package com.galive.logic.network.http.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.exception.LogicException;
import com.galive.logic.helper.LoggerHelper;
import com.galive.logic.model.User;
import com.galive.logic.network.http.HttpRequestHandler;
import com.galive.logic.network.http.handler.LoginHandler.LoginOut;
import com.galive.logic.network.model.RespLoginUser;
import com.galive.logic.service.LoggerService;
import com.galive.logic.service.LoggerServiceImpl;
import com.galive.logic.service.UserService;
import com.galive.logic.service.UserServiceImpl;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandIn;
import com.galive.common.protocol.CommandOut;

@HttpRequestHandler(desc = "用户注册", command = Command.USR_REGISTER)
public class RegisterHandler extends HttpBaseHandler {

	private static Logger logger = LoggerFactory.getLogger(RegisterHandler.class);

	private UserService userService = new UserServiceImpl(logBuffer);
	private LoggerService loggerService = new LoggerServiceImpl();
	
	@Override
	public String handle(String userSid, String reqData) {
		try {
			LoggerHelper.appendLog("--用户注册--", logBuffer);
			RegisterIn in = JSON.parseObject(reqData, RegisterIn.class);
			User u = userService.register(in.username, in.password, in.nickname);
			
			RegisterOut out = new RegisterOut();
			out.token = userService.createToken(u.getSid());
			out.expire = ApplicationConfig.getInstance().getTokenExpire();
			RespLoginUser respLoginUser = new RespLoginUser();
			respLoginUser.convert(u);
			out.user = respLoginUser;
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

	public static class RegisterIn extends CommandIn {
		public String username;
		public String password;
		public String nickname;
	}
	
	public static class RegisterOut extends LoginOut {
		
		public RegisterOut() {
			super();
			setCommand(Command.USR_REGISTER);
		}
	}
	
	private String respFail(String message) {
		String resp = CommandOut.failureOut(Command.USR_REGISTER, message).httpResp();
		return resp;
	}
}
