package com.galive.logic.network.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandIn;
import com.galive.logic.ApplicationMain;
import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.config.SocketConfig;
import com.galive.logic.manager.AnnotationManager;
import com.galive.logic.network.socket.handler.SocketBaseHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class ChannelHandler extends ChannelInboundHandlerAdapter {

	private static Logger logger = LoggerFactory.getLogger(ApplicationMain.class);
	
	private static String KEEP_ALIVE_REQ;
	private static String KEEP_ALIVE_RESP;
	
	static {
		SocketConfig config = ApplicationConfig.getInstance().getSocketConfig();
		KEEP_ALIVE_REQ = config.getLiveReq();
		KEEP_ALIVE_RESP = config.getLiveResp() + config.getMessageDelimiter();
	}
	
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    	printLog("channelRegistered", ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {    
    	printLog("channelUnregistered", ctx);
    	String userSid = ctx.attr(ChannelManager.USER_SID_KEY).get();   
		if (userSid != null) {
			CommandIn in = new CommandIn();
			in.setUserSid(userSid);
			in.setCommand(Command.USR_OFFLINE);
			SocketBaseHandler handler = AnnotationManager.createSocketHandlerInstance(in.getCommand());
			handler.handle(in, ctx);
		}
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	printLog("channelActive", ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	printLog("channelInactive", ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	printLog("channelRead", ctx);
    	String reqData = (String) msg;
    	// 心跳
    	if (keepAlive(reqData, ctx)) {
    		return;
    	}
    	logger.debug(reqData);
		CommandIn in = CommandIn.fromSocketReq(reqData, ApplicationConfig.getInstance().getSocketConfig().getParamsDelimiter());
		if (in != null) {
			SocketBaseHandler handler = AnnotationManager.createSocketHandlerInstance(in.getCommand());
			if (handler != null) {
				handler.handle(in, ctx);
			} else {
				logger.error("channelRead 消息错误:" + reqData);
				closeAndRemoveChannel(ctx);
			}
		} else {
			logger.error("channelRead 消息错误:" + reqData);
			closeAndRemoveChannel(ctx);
		}
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    	printLog("channelReadComplete", ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    	printLog("userEventTriggered", ctx);
    	/*心跳处理*/
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            IdleState state = event.state();
            logger.debug(state.toString());
            if (state == IdleState.ALL_IDLE) {
            	closeAndRemoveChannel(ctx);
            }
        }
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
    	printLog("channelWritabilityChanged", ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	printLog("exceptionCaught", ctx);
//    	logger.debug("exceptionCaught");
//    	logger.error(cause.getMessage());
//    	closeChannel(ctx);
    }
    
    private boolean keepAlive(String message, ChannelHandlerContext ctx) {
    	if (message.equals(KEEP_ALIVE_REQ)) {
    		ctx.writeAndFlush(KEEP_ALIVE_RESP);
    		return true;
    	}
    	return false;
    }
    
    private void closeAndRemoveChannel(ChannelHandlerContext ctx) {
    	String userSid = ctx.attr(ChannelManager.USER_SID_KEY).get();   
		if (userSid != null) {
			ChannelManager.getInstance().closeAndRemoveChannel(userSid);
		} else {
			ChannelManager.closeChannel(ctx);
		}
    }
    
    private void printLog(String message, ChannelHandlerContext ctx) {
    	String ip = ctx.channel().remoteAddress().toString();  
    	logger.debug("client:" + ip + " " + message);
    }
}
