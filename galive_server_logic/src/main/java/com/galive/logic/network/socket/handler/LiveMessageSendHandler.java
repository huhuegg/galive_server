package com.galive.logic.network.socket.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.exception.LogicException;
import com.galive.logic.helper.LoggerHelper;
import com.galive.logic.model.Live;
import com.galive.logic.model.User;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.LiveMessagePush;
import com.galive.logic.service.LiveService;
import com.galive.logic.service.LiveServiceImpl;
import com.galive.logic.service.LoggerService;
import com.galive.logic.service.LoggerServiceImpl;
import com.galive.logic.service.UserService;
import com.galive.logic.service.UserServiceImpl;

@SocketRequestHandler(desc = "发送直播消息", command = Command.LIVE_MESSAGE_SEND)
public class LiveMessageSendHandler extends SocketBaseHandler {

	private static Logger logger = LoggerFactory.getLogger(LiveMessageSendHandler.class);

	private UserService userService = new UserServiceImpl(logBuffer);
	private LiveService liveService = new LiveServiceImpl(logBuffer);
	private LoggerService loggerService = new LoggerServiceImpl();

	@Override
	public String handle(String userSid, String reqData) {
		try {
			LoggerHelper.appendLog("--发送直播消息--", logBuffer);
			
			LiveMessageSendIn in = JSON.parseObject(reqData, LiveMessageSendIn.class);
			
			Live live = liveService.findLiveByUser(userSid);
			if (live != null) {
				User sender = userService.findUserBySid(userSid);
				LoggerHelper.appendLog(sender.desc() + "发送直播消息:" + in.content, logBuffer);

				// 推送
				List<String> audienceSids = liveService.listAllAudiences(live.getSid());
				LiveMessagePush push = new LiveMessagePush();
				push.content = in.content;
				String pushMessage = push.socketResp();
				LoggerHelper.appendLog("LIVE_MESSAGE_PUSH:" + pushMessage, logBuffer);
				LoggerHelper.appendLog("推送用户数量:" + audienceSids.size(), logBuffer);
				for (String sid : audienceSids) {
					if (!sid.equals(userSid)) {
						if (userService.isOnline(sid)) {
							pushMessage(sid, pushMessage);
						}
					}
				}
			}
			
			CommandOut out = new CommandOut(Command.LIVE_MESSAGE_SEND);
			String resp = out.socketResp();
			LoggerHelper.appendLog("响应客户端:" + resp, logBuffer);
			LoggerHelper.appendSplit(logBuffer);
			String logicLog = LoggerHelper.loggerString(logBuffer);
			logger.info(logicLog);
			loggerService.saveLogicLog(logicLog);
			return resp;
		} catch (LogicException e) {
			String resp = respFail(e.getMessage());
			LoggerHelper.appendLog("响应客户端:" + resp, logBuffer);
			LoggerHelper.appendSplit(logBuffer);
			String logicLog = LoggerHelper.loggerString(logBuffer);
			logger.error(logicLog);
			loggerService.saveLogicLog(logicLog);
			return resp;
		} catch (Exception e) {
			String resp = respFail(null);
			LoggerHelper.appendLog("发生错误" + e.getMessage(), logBuffer);
			LoggerHelper.appendLog("响应客户端|" + resp, logBuffer);
			LoggerHelper.appendSplit(logBuffer);
			String logicLog = LoggerHelper.loggerString(logBuffer);
			logger.error(logicLog);
			loggerService.saveLogicLog(logicLog);
			return resp;
		}

	}

	public static class LiveMessageSendIn {

		public String content;
	}

	private String respFail(String message) {
		String resp = CommandOut.failureOut(Command.LIVE_MESSAGE_SEND, message).httpResp();
		return resp;
	}

}
