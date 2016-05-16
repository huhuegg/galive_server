package com.galive.logic.network.http.handler;

import com.alibaba.fastjson.JSON;
import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.model.User;
import com.galive.logic.model.User.UserGender;
import com.galive.logic.network.http.HttpRequestHandler;
import com.galive.logic.network.http.handler.LoginHandler.LoginOut;
import com.galive.logic.network.model.RespLoginUser;
import com.galive.logic.service.UserService;
import com.galive.logic.service.UserServiceImpl;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandIn;

@HttpRequestHandler(desc = "用户注册", command = Command.USR_REGISTER)
public class RegisterHandler extends HttpBaseHandler {

	private UserService userService = new UserServiceImpl();
	
	@Override
	public String handle(String userSid, String reqData) throws Exception {
		appendLog("--RegisterHandler(用户注册)--");
		RegisterIn in = JSON.parseObject(reqData, RegisterIn.class);
		
		String username = in.username;
		String password = in.password;
		String nickname = in.nickname;
		String avatar = in.avatar;
		int gender = in.gender;
		String profile = in.profile;
		String deviceid = in.deviceid;
		
		appendLog("用户名:" + username);
		appendLog("密码:" + password);
		appendLog("昵称:" + nickname);
		appendLog("头像:" + avatar);
		appendLog("性别:" + gender);
		appendLog("简述:" + profile);
		appendLog("设备id:" + deviceid);
		
		UserGender g = UserGender.convert(gender);
		User u = userService.register(deviceid, username, password, nickname, avatar, g, profile);
		
		RegisterOut out = new RegisterOut();
		out.token = userService.createToken(u.getSid());
		out.expire = ApplicationConfig.getInstance().getTokenExpire();
		RespLoginUser respLoginUser = new RespLoginUser();
		respLoginUser.convert(u);
		out.user = respLoginUser;
		String resp = out.httpResp();
		return resp;
		
	}

	public static class RegisterIn extends CommandIn {
		public String username;
		public String password;
		public String nickname;
		public String avatar;
		public String profile;
		public int gender;
		public String deviceid;
	}
	
	public static class RegisterOut extends LoginOut {
		
		public RegisterOut() {
			super();
			setCommand(Command.USR_REGISTER);
		}
	}
	
}
