package com.galive.logic.network.socket.handler;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Room;
import com.galive.logic.model.User;
import com.galive.logic.network.model.RespUser;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.RoomService;
import com.galive.logic.service.RoomServiceImpl;
import com.galive.logic.service.UserService;
import com.galive.logic.service.UserServiceImpl;

@SocketRequestHandler(desc = "获取用户信息", command = Command.USR_INFO)
public class UserInfoHandler extends SocketBaseHandler {

	private UserService userService = new UserServiceImpl();
	private RoomService roomService = new RoomServiceImpl();
	
	@Override
	public CommandOut handle(String userSid, String reqData) throws Exception {
		appendLog("--UserInfoHandler(获取用户信息)--");
		UserInfoIn in = JSON.parseObject(reqData, UserInfoIn.class);
		String uid = in.userSid;
		
		appendLog("目标用户id(userSid):" + uid);
		
		User u = userService.findUserBySid(uid);
		UserInfoOut out = new UserInfoOut();
		RespUser ru = new RespUser();
		ru.convert(u);
		Room room = roomService.findRoomByUser(u.getSid());
		if (room != null) {
			ru.roomSid = room.getSid();
		}
		out.user = ru;
		return out;
	}

	public static class UserInfoIn {
		public String userSid;
	}
	
	public static class UserInfoOut extends CommandOut {

		public RespUser user;
		
		public UserInfoOut() {
			super(Command.USR_INFO); 
		}
	}
}
