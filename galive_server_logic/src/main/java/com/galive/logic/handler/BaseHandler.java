package com.galive.logic.handler;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandIn;
import com.galive.common.protocol.CommandOut;
import com.galive.common.protocol.RetCode;
import com.galive.logic.ChannelManager;
import com.galive.logic.model.User;

import io.netty.channel.ChannelHandlerContext;

public abstract class BaseHandler {

	private static Logger logger = LoggerFactory.getLogger(BaseHandler.class);
	
	public abstract CommandOut commandProcess(String userSid, String reqData);
	
	public String processHttpRequest(CommandIn in, String reqData) {
		CommandOut out = null;
		String command = in.getCommand();
		String userSid = in.getUserSid();
		String token = in.getToken();
		try {
			if ((!command.equals(Command.USR_LOGIN) && !command.equals(Command.USR_REGISTER))  && !User.verifyToken(userSid, token)) {
				// 验证token
				out = CommandOut.failureOut(command, "登录已过期");
				out.setRet_code(RetCode.TOKEN_EXPIRE);
			} else {
				out = commandProcess(userSid, reqData);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户" + userSid + " 请求失败:" + e.getMessage());
			out = CommandOut.failureOut(command, e.getMessage());
		} 
		String respBody = out.toJson();
		logger.debug("响应数据|" + respBody);
		return respBody;
	}
	
	public void processSocketRequest(CommandIn in, String reqData, ChannelHandlerContext channel) {
		try {
			String command = in.getCommand();
			String userSid = in.getUserSid();
			String token = in.getToken();
			// 客户端打开连接
			if (command.equals(Command.USR_ONLINE)) {
				if (!StringUtils.isBlank(userSid)) {
					if (!User.verifyToken(userSid, token)) {
						ChannelManager.getInstance().removeChannel(userSid);
						return;
					}
					ChannelManager.getInstance().addChannel(userSid, channel);
				}
			} else if (command.equals(Command.USR_OFFLINE)) {
				ChannelManager.getInstance().removeChannel(userSid);
			} else {
				if (!command.equals(Command.CLIENT_TRANSMIT) && !User.verifyToken(userSid, token)) {
					// token失效
					CommandOut out = CommandOut.failureOut(Command.KICK_OFF_PUSH, "您已被踢下线");
					ChannelManager.getInstance().sendMessage(userSid, out.toJson());
					return;
				}
			}
			CommandOut out = commandProcess(userSid, reqData);
			ChannelManager.getInstance().sendMessage(userSid, out.toJson());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
	}
//	
//	public String process(CommandIn in, String reqData) {
//		CommandOut out = null;
//		String command = in.getCommand();
//		String userSid = in.getUserSid();
//		String token = in.getToken();
//		try {
//			if (command.equals(Command.USR_LOGIN))
//			
//			
//			
//			
//			
//			
//			if ((!command.equals(Command.USR_LOGIN) && !command.equals(Command.USR_REGISTER))  && !User.verifyToken(userSid, token)) {
//				// 验证token
//				out = CommandOut.failureOut(command, "登录已过期");
//				out.setRet_code(RetCode.TOKEN_EXPIRE);
//			} else {
//				out = commandProcess(userSid, reqData);
//			}
//			
//			
//			// 客户端打开连接
//			if (command.equals(Command.USR_ONLINE)) {
//				if (!StringUtils.isBlank(userSid)) {
//					if (!User.verifyToken(userSid, token)) {
//						return null;
//					}
//					WSClients.put(userSid, session);
//				}
//			} else if (commandID.equals(CommandID.USER_OFFLINE)) {
//				WSClients.remove(userSid);
//			} else {
//				if (!commandID.equals(CommandID.CLIENT_TRANSMIT) && !User.verifyToken(userSid, token)) {
//					// token失效
//					CommandOut out = CommandOut.failureCommand(CommandID.KICK_OFF_PUSH, "您已被踢下线");
//					sendMessage(userSid, out);
//					return;
//				}
//			}
//			commandProcess(userSid, reqData);
//			
//			
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("用户" + userSid + " 请求失败:" + e.getMessage());
//			out = CommandOut.failureOut(command, e.getMessage());
//		} 
//		String json = out.toJson();
//		logger.debug("响应数据|" + json);
//		return json;
//	}
	
	protected void pushMessage(String userSid, CommandOut out) {
		String json = out.toJson();
		ChannelManager.getInstance().sendMessage(userSid, json);
	}
	
}
