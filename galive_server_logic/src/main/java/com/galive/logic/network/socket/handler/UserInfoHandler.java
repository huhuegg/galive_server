package com.galive.logic.network.socket.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandIn;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Room;
import com.galive.logic.model.User;
import com.galive.logic.network.model.RespUser;
import com.galive.logic.network.socket.SocketRequestHandler;

@SocketRequestHandler(desc = "获取用户信息", command = Command.USR_INFO)
public class UserInfoHandler extends SocketBaseHandler {

	private static Logger logger = LoggerFactory.getLogger(UserInfoHandler.class);

	@Override
	public String handle(String userSid, String reqData) {
		try {
			logger.debug("获取用户信息|" + reqData);
			UserInfoRequest req = JSON.parseObject(reqData, UserInfoRequest.class);
			User u = userService.findUserBySid(req.userSid);
			UserInfoOut out = new UserInfoOut();
			RespUser ru = RespUser.convert(u);
			Room room = roomService.findRoomByUser(u.getSid());
			if (room != null) {
				ru.roomSid = room.getSid();
			}
			out.user = ru;
			String resp = out.socketResp();
			logger.debug("获取用户信息|" + resp);
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

	public static class UserInfoRequest extends CommandIn {
		public String userSid;
	}
	
	public static class UserInfoOut extends CommandOut {

		public RespUser user;
		
		public UserInfoOut() {
			super(Command.USR_INFO); 
		}
	}
	
	private String respFail(String message) {
		String resp = CommandOut.failureOut(Command.USR_LOGIN, message).httpResp();
		logger.error("获取用户信息失败|" + resp);
		return resp;
	}
}
