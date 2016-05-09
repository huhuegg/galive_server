package com.galive.logic.network.http.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandIn;
import com.galive.common.protocol.CommandOut;
import com.galive.common.protocol.RetCode;
import com.galive.logic.exception.LogicException;
import com.galive.logic.service.UserService;
import com.galive.logic.service.UserServiceImpl;

public abstract class HttpBaseHandler {
	
	private static Logger logger = LoggerFactory.getLogger(HttpBaseHandler.class);
	
	public abstract String handle(String userSid, String reqData) throws Exception;
	
	protected StringBuffer logBuffer = new StringBuffer();
	
	public String handle(CommandIn in) {
		appendSplit();
		long start = System.currentTimeMillis();
		String resp = null;
		String command = in.getCommand();
		String userSid = in.getUserSid();
		String token = in.getToken();
		String params = in.getParams();
		appendLog("请求参数:");
		appendLog("command:" + command);
		appendLog("userSid:" + userSid);
		appendLog("token:" + token);
		appendLog("params:" + params);
		try {
			if (!command.equals(Command.USR_LOGIN) && !command.equals(Command.USR_REGISTER) && !command.equals(Command.USR_LOGIN_PLATFORM)) {
				UserService userService = new UserServiceImpl();
				if (!userService.verifyToken(userSid, token)) {
					// 验证token
					CommandOut out = CommandOut.failureOut(command, "登录已过期");
					out.setRet_code(RetCode.TOKEN_EXPIRE);
					resp = out.httpResp();
					appendLog("验证token失败,登录已过期。");
				} else {
					resp = handle(userSid, params);
				}
			} else {
				resp = handle(userSid, params);
			}
		} catch (LogicException logicException) {
			logicException.printStackTrace();
			String error = logicException.getMessage();
			appendLog("逻辑错误:" + error);
			resp = respFail(error, command);
		} catch (Exception exception) {
			exception.printStackTrace();
			String error = exception.getMessage();
			appendLog("发生错误:" + error);
			resp = respFail(error, command);
		}
		appendLog("响应:");
		appendLog(resp);
		appendLog("处理时间:" + (System.currentTimeMillis() - start) + " ms");
		appendSplit();
		logger.info(loggerString());
		return resp;
	}
	
	private String respFail(String message, String command) {
		String resp = CommandOut.failureOut(command, message).httpResp();
		return resp;
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
