package com.galive.logic.network.socket.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandIn;
import com.galive.logic.network.socket.ChannelManager;
import com.galive.logic.network.socket.handler.push.KickOffPush;
import io.netty.channel.ChannelHandlerContext;

public abstract class SocketBaseHandler {

	private static Logger logger = LoggerFactory.getLogger(SocketBaseHandler.class);
	
	public abstract String handle(String userSid, String reqData);
	
	protected StringBuffer loggerBuffer = new StringBuffer();

	public void handle(CommandIn in, ChannelHandlerContext channel) {
		try {
			String command = in.getCommand();
			String userSid = in.getUserSid();
			// 客户端打开连接
			if (command.equals(Command.USR_ONLINE)) {
				// 是否不同设备连接
				ChannelHandlerContext old = ChannelManager.getInstance().findChannel(userSid);
				if (old != null) {
					logger.debug(userSid + "|用户重复登录，将帐号踢下线。");
					KickOffPush push = new KickOffPush();
					pushMessage(userSid, push.socketResp());
				}
				ChannelManager.getInstance().addChannel(userSid, channel);
			}
			String out = handle(userSid, in.getParams());
			ChannelManager.getInstance().sendMessage(userSid, out);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
	}
	
	protected void pushMessage(String userSid, String message) {
		ChannelManager.getInstance().sendMessage(userSid, message);
	}
	
	protected void appendLog(String log) {
		loggerBuffer.append(log);
		loggerBuffer.append("/n");
	}
}
