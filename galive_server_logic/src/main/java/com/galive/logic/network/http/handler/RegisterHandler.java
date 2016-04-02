package com.galive.logic.network.http.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.model.User;
import com.galive.logic.network.http.HttpRequestHandler;
import com.galive.logic.network.http.handler.LoginHandler.LoginOut;
import com.galive.logic.network.model.RespUser;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandIn;

@HttpRequestHandler(desc = "用户注册", command = Command.USR_REGISTER)
public class RegisterHandler extends HttpBaseHandler {

	private static Logger logger = LoggerFactory.getLogger(RegisterHandler.class);

	@Override
	public CommandOut handle(String userSid, String reqData) {
		logger.debug("用户注册|" + reqData);
		RegisterIn in = JSON.parseObject(reqData, RegisterIn.class);
		User u = User.findByUsername(in.username);
		if (u != null) {
			return CommandOut.failureOut(Command.USR_REGISTER, "用户已注册");
		}
		u = new User();
		u.setUsername(in.username);
		u.setPassword(in.password);
		u.setNickname(in.nickname);
		u.save();
		u.updateLatestLogin();
		
		RegisterOut out = new RegisterOut();
		out.token = User.updateToken(u.getSid());
		out.expire = ApplicationConfig.getInstance().getTokenExpire();
		out.user = RespUser.convertFromUser(u);
		return out;
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
}
