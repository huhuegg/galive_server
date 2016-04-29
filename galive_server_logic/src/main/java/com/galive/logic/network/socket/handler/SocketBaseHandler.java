package com.galive.logic.network.socket.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandIn;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.exception.LogicException;
import com.galive.logic.network.socket.ChannelManager;
import com.galive.logic.network.socket.handler.push.KickOffPush;
import io.netty.channel.ChannelHandlerContext;

public abstract class SocketBaseHandler {
	
	private static Logger logger = LoggerFactory.getLogger(SocketBaseHandler.class);
	
	public abstract CommandOut handle(String userSid, String reqData) throws Exception;

	protected StringBuffer logBuffer = new StringBuffer();
	
	public void handle(CommandIn in, ChannelHandlerContext channel) {
		appendSplit();
		long start = System.currentTimeMillis();
		CommandOut out = null;
		String command = in.getCommand();
		String userSid = in.getUserSid();
		String token = in.getToken();
		String params = in.getParams();
		appendLog("请求参数");
		appendLog("command:" + command);
		appendLog("userSid:" + userSid);
		appendLog("token:" + token);
		appendLog("params:" + params);
		
		try {
			// 客户端打开连接
			if (command.equals(Command.USR_ONLINE)) {
				// 是否不同设备连接
				ChannelHandlerContext old = ChannelManager.getInstance().findChannel(userSid);
				if (old != null) {
					KickOffPush push = new KickOffPush();
					pushMessage(userSid, push.socketResp());
					appendLog("用户重复登录，将帐号踢下线。");
				}
				ChannelManager.getInstance().addChannel(userSid, channel);
			}
			out = handle(userSid, in.getParams());
		} catch (LogicException logicException) {
			logicException.printStackTrace();
			String error = logicException.getMessage();
			appendLog("逻辑错误:" + error);
			out = respFail(error, command);
		} catch (Exception exception) {
			exception.printStackTrace();
			String error = exception.getMessage();
			appendLog("发生错误:" + error);
			out = respFail(error, command);
		}
		appendLog("响应:");
		appendLog(out);
		appendLog("处理时间:" + (System.currentTimeMillis() - start) + " ms");
		appendSplit();
		logger.info(loggerString());
		ChannelManager.getInstance().sendMessage(userSid, out);
	}
	
	protected void pushMessage(String userSid, String message) {
		ChannelManager.getInstance().sendMessage(userSid, message);
	}
	
	private CommandOut respFail(String message, String command) {
		CommandOut out = CommandOut.failureOut(command, message);
		return out;
	}
	
	protected void appendLog(String log) {
		if (logBuffer != null) {
			logBuffer.append("|" + log);
			logBuffer.append("\n");
		}
	}
	
	protected void appendSplit() {
		if (logBuffer != null) {
			logBuffer.append("\n＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝\n");
		}
	}
	
	protected String loggerString() {
		return logBuffer == null ? "" : logBuffer.toString();
	}

}
