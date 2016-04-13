package com.galive.logic.network.http.handler;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandIn;
import com.galive.common.protocol.CommandOut;
import com.galive.common.protocol.RetCode;
import com.galive.logic.helper.LoggerHelper;
import com.galive.logic.service.UserService;
import com.galive.logic.service.UserServiceImpl;

public abstract class HttpBaseHandler {
	
	public abstract String handle(String userSid, String reqData);
	
	protected StringBuffer logBuffer = new StringBuffer();
	
	public String handle(CommandIn in) {
		LoggerHelper.appendSplit(logBuffer);
		String resp = null;
		String command = in.getCommand();
		String userSid = in.getUserSid();
		String token = in.getToken();
		String params = in.getParams();
		LoggerHelper.appendLog("====请求参数====", logBuffer);
		LoggerHelper.appendLog("command:" + command, logBuffer);
		LoggerHelper.appendLog("userSid:" + userSid, logBuffer);
		LoggerHelper.appendLog("token:" + token, logBuffer);
		LoggerHelper.appendLog("params:" + params, logBuffer);
		LoggerHelper.appendLog("==============", logBuffer);
		if ((!command.equals(Command.USR_LOGIN) && !command.equals(Command.USR_REGISTER))) {
			UserService userService = new UserServiceImpl(logBuffer);
			if (!userService.verifyToken(userSid, token)) {
				// 验证token
				CommandOut out = CommandOut.failureOut(command, "登录已过期");
				out.setRet_code(RetCode.TOKEN_EXPIRE);
				resp = out.httpResp();
				LoggerHelper.appendLog("验证token失败,登录已过期。" + params, logBuffer);
			}
		} else {
			resp = handle(userSid, params);
		}
		return resp;
	}

	
}
