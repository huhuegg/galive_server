package com.galive.logic.network.http.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.exception.LogicException;
import com.galive.logic.model.User;
import com.galive.logic.network.http.HttpRequestHandler;
import com.galive.logic.network.http.handler.LoginHandler.LoginOut;
import com.galive.logic.network.model.RespUser;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandIn;
import com.galive.common.protocol.CommandOut;

@HttpRequestHandler(desc = "用户注册", command = Command.USR_REGISTER)
public class RegisterHandler extends HttpBaseHandler {

	private static Logger logger = LoggerFactory.getLogger(RegisterHandler.class);

	@Override
	public String handle(String userSid, String reqData) {
		try {
			logger.debug("用户注册|" + reqData);
			RegisterIn in = JSON.parseObject(reqData, RegisterIn.class);
			User u = userService.register(in.username, in.password, in.nickname);
			
			RegisterOut out = new RegisterOut();
			out.token = userService.createToken(u.getSid());
			out.expire = ApplicationConfig.getInstance().getTokenExpire();
			out.user = RespUser.convert(u);
			String resp = out.httpResp();
			logger.info("用户注册|" + resp);
			return resp;
		} catch (LogicException e) {
			logger.error(e.getMessage());
			String resp = respFail(e.getMessage());
			return resp;
		} catch (Exception e) {
			logger.error(e.getMessage());
			String resp = respFail(null);
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
		logger.error("用户注册失败|" + resp);
		return resp;
	}
}
