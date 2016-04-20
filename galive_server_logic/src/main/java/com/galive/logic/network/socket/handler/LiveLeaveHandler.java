package com.galive.logic.network.socket.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.exception.LogicException;
import com.galive.logic.helper.LoggerHelper;
import com.galive.logic.model.Live;
import com.galive.logic.model.User;
import com.galive.logic.network.model.RespUser;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.LiveLeavePush;
import com.galive.logic.service.LiveService;
import com.galive.logic.service.LiveServiceImpl;
import com.galive.logic.service.LoggerService;
import com.galive.logic.service.LoggerServiceImpl;
import com.galive.logic.service.UserService;
import com.galive.logic.service.UserServiceImpl;

@SocketRequestHandler(desc = "退出观看直播", command = Command.LIVE_LEAVE)
public class LiveLeaveHandler extends SocketBaseHandler  {

	private static Logger logger = LoggerFactory.getLogger(LiveLeaveHandler.class);

	private LiveService liveService = new LiveServiceImpl(logBuffer);
	private UserService userService = new UserServiceImpl(logBuffer);
	private LoggerService loggerService = new LoggerServiceImpl();
	
	@Override
	public String handle(String userSid, String reqData) {
		try {
			LoggerHelper.appendLog("--退出观看直播--", logBuffer);
			
			Live live = liveService.leaveLive(userSid);
			if (live != null) {
				// 推送
				List<String> audienceSids = liveService.listAllAudiences(live.getSid());
				LiveLeavePush push = new LiveLeavePush();
				RespUser respUser = new RespUser();
				User user = userService.findUserBySid(userSid);
				respUser.convert(user);
				push.user = respUser;
				String pushMessage = push.socketResp();
				LoggerHelper.appendLog("LIVE_LEAVE_PUSH:" + pushMessage, logBuffer);
				LoggerHelper.appendLog("推送用户数量:" + audienceSids.size(), logBuffer);
				for (String sid : audienceSids) {
					if (!sid.equals(userSid)) {
						if (userService.isOnline(sid)) {
							pushMessage(sid, pushMessage);
						}
					}
				}
			}
			
			
			CommandOut out = new CommandOut(Command.LIVE_LEAVE);
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
	
	private String respFail(String message) {
		String resp = CommandOut.failureOut(Command.LIVE_LEAVE, message).httpResp();
		return resp;
	}
	
}
