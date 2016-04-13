package com.galive.logic.network.http.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandIn;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.exception.LogicException;
import com.galive.logic.helper.LoggerHelper;
import com.galive.logic.network.http.HttpRequestHandler;
import com.galive.logic.service.LoggerService;
import com.galive.logic.service.LoggerServiceImpl;
import com.galive.logic.service.UserServiceImpl;

@HttpRequestHandler(desc = "修改用户信息", command = Command.USR_INFO_MODIFY)
public class UserInfoModifyHandler extends HttpBaseHandler {

	public enum UserInfoModifyType {
		DeviceToken;
	}
	
	private static Logger logger = LoggerFactory.getLogger(UserInfoModifyHandler.class);
	
	private UserServiceImpl userService = new UserServiceImpl(logBuffer);
	private LoggerService loggerService = new LoggerServiceImpl();
	
	@Override
	public String handle(String userSid, String reqData) {
		try {
			LoggerHelper.appendLog("--修改用户信息--", logBuffer);
			UserInfoModifyIn req = JSON.parseObject(reqData, UserInfoModifyIn.class);
			
			int type = req.type;
			if (type == UserInfoModifyType.DeviceToken.ordinal()) {
				// 更新deviceToken
				userService.updateDeviceToken(userSid, req.deviceToken);
			}
			CommandOut out = new CommandOut(Command.USR_INFO_MODIFY);
			String resp = out.httpResp();
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
			LoggerHelper.appendLog("发生错误:" + e.getMessage(), logBuffer);
			LoggerHelper.appendLog("响应客户端:" + resp, logBuffer);
			LoggerHelper.appendSplit(logBuffer);
			String logicLog = LoggerHelper.loggerString(logBuffer);
			logger.error(logicLog);
			loggerService.saveLogicLog(logicLog);
			return resp;
		}
		
	}

	public static class UserInfoModifyIn extends CommandIn {
		public int type;
		public String deviceToken;
	}

	private String respFail(String message) {
		String resp = CommandOut.failureOut(Command.USR_LOGIN, message).httpResp();
		return resp;
	}
}
