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
import com.galive.logic.model.Room;
import com.galive.logic.model.User;
import com.galive.logic.network.http.HttpRequestHandler;
import com.galive.logic.network.model.RespRoom;
import com.galive.logic.network.model.RespUser;

@HttpRequestHandler(desc = "用户登录", command = Command.USR_LOGIN)
public class LoginHandler extends HttpBaseHandler {

	private static Logger logger = LoggerFactory.getLogger(LoginHandler.class);

	@Override
	public CommandOut handle(String userSid, String reqData) {
		logger.debug("用户登录|" + reqData);
		LoginIn in = JSON.parseObject(reqData, LoginIn.class);
		User u = User.findByUsername(in.username);
		if (u == null) {
			return CommandOut.failureOut(Command.USR_LOGIN, "用户不存在");
		} else {
			if (!u.getPassword().equals(in.password)) {
				return CommandOut.failureOut(Command.USR_LOGIN, "密码错误");
			}
		}
		u.updateLatestLogin();
		
		LoginOut out = new LoginOut();
		Room room = Room.findRoomByUser(u.getSid());
		if (room != null) {
			out.room = RespRoom.convertFromUserRoom(room);
		}
		RespUser ru = RespUser.convertFromUser(u);
		out.token = User.updateToken(u.getSid());
		out.expire = ApplicationConfig.getInstance().getTokenExpire();
		out.user = ru;
		out.socket_config = ApplicationConfig.getInstance().getSocketConfig();
		return out;
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
		public RespRoom room;
		public SocketConfig socket_config;

	}


}
