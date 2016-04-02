package com.galive.logic.network.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandIn;
import com.galive.logic.ApplicationMain;
import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.config.SocketConfig;
import com.galive.logic.helper.AnnotationManager;
import com.galive.logic.network.socket.handler.SocketBaseHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class LogicHandler extends ChannelInboundHandlerAdapter {

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
//    	String userSid = ctx.attr(ChannelManager.USER_SID_KEY).get();   
//		if (userSid != null) {
//			//传递给handler处理业务逻辑
//			String cmd = Command.USR_OFFLINE;
//			SocketBaseHandler handler = AnnotationManager.createLogicHandlerInstance(cmd);
//			CommandIn in = new CommandIn();
//			in.setCommand(Command.USR_OFFLINE);
//			handler.processSocketRequest(in, null, ctx);
//			logger.debug("===> channelUnregistered:" + userSid);
//		}
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	printLog("channelActive", ctx);
//    	logger.debug("channelActive");
//    	String ip = ctx.channel().remoteAddress().toString();  
//    	logger.debug("client:" + ip + " channelUnregistered");
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
//		CommandIn in = JSON.parseObject(reqData, CommandIn.class);
//		String command = in.getCommand();
//		//传递给handler处理业务逻辑
//		SocketBaseHandler handler = AnnotationManager.createLogicHandlerInstance(command);
//		if (handler != null) {
//			handler.processSocketRequest(in, reqData, ctx);
//		} else {
//			// 无效请求
//			logger.error("请求失败:参数错误|" + reqData);
//			closeChannel(ctx);
//		}
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
            	String userSid = ctx.attr(ChannelManager.USER_SID_KEY).get();   
        		if (userSid != null) {
        			ChannelManager.getInstance().closeAndRemoveChannel(userSid);
        		} else {
					ChannelManager.closeChannel(ctx);
        		}
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
    
    private void printLog(String method, ChannelHandlerContext ctx) {
    	String ip = ctx.channel().remoteAddress().toString();  
    	logger.debug("client:" + ip + " " + method);
    }
}
