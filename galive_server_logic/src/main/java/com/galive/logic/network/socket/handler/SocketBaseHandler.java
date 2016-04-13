package com.galive.logic.network.socket.handler;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandIn;
import com.galive.logic.helper.LoggerHelper;
import com.galive.logic.network.socket.ChannelManager;
import com.galive.logic.network.socket.handler.push.KickOffPush;
import io.netty.channel.ChannelHandlerContext;

public abstract class SocketBaseHandler {
	
	public abstract String handle(String userSid, String reqData);

	protected StringBuffer logBuffer = new StringBuffer();
	
	public void handle(CommandIn in, ChannelHandlerContext channel) {
		String command = in.getCommand();
		String userSid = in.getUserSid();
		LoggerHelper.appendLog("====请求参数====", logBuffer);
		LoggerHelper.appendLog("command:" + command, logBuffer);
		LoggerHelper.appendLog("userSid:" + userSid, logBuffer);
		LoggerHelper.appendLog("==============", logBuffer);
		// 客户端打开连接
		if (command.equals(Command.USR_ONLINE)) {
			// 是否不同设备连接
			ChannelHandlerContext old = ChannelManager.getInstance().findChannel(userSid);
			if (old != null) {
				KickOffPush push = new KickOffPush();
				pushMessage(userSid, push.socketResp());
				LoggerHelper.appendLog("用户重复登录，将帐号踢下线。", logBuffer);
			}
			ChannelManager.getInstance().addChannel(userSid, channel);
		}
		String out = handle(userSid, in.getParams());
		ChannelManager.getInstance().sendMessage(userSid, out);
	}
	
	protected void pushMessage(String userSid, String message) {
		ChannelManager.getInstance().sendMessage(userSid, message);
	}
	
}
