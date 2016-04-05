package com.galive.logic.network.http.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandIn;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.config.RTCConfig;
import com.galive.logic.config.SocketConfig;
import com.galive.logic.exception.LogicException;
import com.galive.logic.model.User;
import com.galive.logic.network.http.HttpRequestHandler;
import com.galive.logic.network.model.RespUser;

@HttpRequestHandler(desc = "用户登录", command = Command.USR_LOGIN)
public class LoginHandler extends HttpBaseHandler {

	private static Logger logger = LoggerFactory.getLogger(LoginHandler.class);
	
	@Override
	public String handle(String userSid, String reqData) {
		try {
			logger.debug("用户登录|" + reqData);
			LoginIn in = JSON.parseObject(reqData, LoginIn.class);

			User u = userService.login(in.username, in.password);

			LoginOut out = new LoginOut();
			RespUser respUser = RespUser.convert(u);
			
			
			out.token =  userService.createToken(u.getSid());
			out.expire = ApplicationConfig.getInstance().getTokenExpire();
			out.user = respUser;
			out.socket_config = ApplicationConfig.getInstance().getSocketConfig();
			String resp = out.httpResp();
			logger.info("用户登录|" + resp);
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

	public static class LoginIn extends CommandIn {
		public String username;
		public String password;
	}
	
	public static class LoginOut extends CommandOut {

		public LoginOut() {
			super(Command.USR_LOGIN);
			rtc_config = ApplicationConfig.getInstance().getRtcConfig();
		}

		public RespUser user;
		public String token;
		public String websocketUrl;
		public int expire;
		public RTCConfig rtc_config;
		public SocketConfig socket_config;
	}
	
	private String respFail(String message) {
		String resp = CommandOut.failureOut(Command.USR_LOGIN, message).httpResp();
		logger.error("用户登录失败|" + resp);
		return resp;
	}


}
