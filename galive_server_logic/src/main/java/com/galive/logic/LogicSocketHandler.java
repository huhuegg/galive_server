package com.galive.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandIn;
import com.galive.logic.handler.BaseHandler;
import com.galive.logic.helper.AnnotationManager;
import com.galive.logic.model.User;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class LogicSocketHandler extends ChannelInboundHandlerAdapter {

	private static Logger logger = LoggerFactory.getLogger(ApplicationMain.class);
	
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    	String ip = ctx.channel().remoteAddress().toString();  
    	logger.debug("client:" + ip + " channelRegistered");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {    
    	String userSid = ctx.attr(ChannelManager.USER_SID_KEY).get();   
		if (userSid != null) {
			//传递给handler处理业务逻辑
			String cmd = Command.USR_OFFLINE;
			BaseHandler handler = AnnotationManager.createLogicHandlerInstance(cmd);
			CommandIn in = new CommandIn();
			in.setCommand(Command.USR_OFFLINE);
			handler.processSocketRequest(in, null, ctx);
			logger.debug("===> channelUnregistered:" + userSid);
		}
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	logger.debug("channelActive");
    	String ip = ctx.channel().remoteAddress().toString();  
    	logger.debug("client:" + ip + " channelUnregistered");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	logger.debug("===> channelInactive");
    	closeChannel(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	logger.debug("channelRead");
    	String reqData = (String) msg;
		CommandIn in = JSON.parseObject(reqData, CommandIn.class);
		String command = in.getCommand();
		String userSid = in.getUserSid();
		String token = in.getToken();
		if (command.equals(Command.USR_ONLINE)) {
			if (User.verifyToken(userSid, token)) {
				
			} else {
				closeChannel(ctx);
			}
		} else {
			//传递给handler处理业务逻辑
			BaseHandler handler = AnnotationManager.createLogicHandlerInstance(command);
			if (handler != null) {
				handler.processSocketRequest(in, reqData, ctx);
			} else {
				// 无效请求
				logger.error("请求失败:参数错误|" + reqData);
				closeChannel(ctx);
			}
		} 
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    	logger.debug("channelReadComplete");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    	logger.debug("userEventTriggered");
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
    	logger.debug("channelWritabilityChanged");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	logger.debug("exceptionCaught");
    	logger.error(cause.getMessage());
    	closeChannel(ctx);
    }
    
    private void closeChannel(ChannelHandlerContext ctx) {
    	String userSid = ctx.attr(ChannelManager.USER_SID_KEY).get();   
		if (userSid != null) {
			ChannelManager.getInstance().removeChannel(userSid);
		} else {
			ctx.flush();
			ctx.close();
		}
    }
}
