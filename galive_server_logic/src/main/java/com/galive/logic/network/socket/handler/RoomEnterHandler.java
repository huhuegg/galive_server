package com.galive.logic.network.socket.handler;

import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Room;
import com.galive.logic.model.User;
import com.galive.logic.network.model.RespRoom;
import com.galive.logic.network.model.RespUser;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.RoomEnterPush;
import com.galive.logic.service.RoomService;
import com.galive.logic.service.RoomServiceImpl;
import com.galive.logic.service.UserService;
import com.galive.logic.service.UserServiceImpl;

@SocketRequestHandler(desc = "进入房间", command = Command.ROOM_ENTER)
public class RoomEnterHandler extends SocketBaseHandler {

	private UserService userService = new UserServiceImpl();
	private RoomService roomService = new RoomServiceImpl();
	
	@Override
	public CommandOut handle(String userSid, String reqData) throws Exception {
		appendLog("--RoomEnterHandler(进入房间)--");
		RoomEnterIn in = JSON.parseObject(reqData, RoomEnterIn.class);
		String roomSid = in.roomSid;
		appendLog("房间(roomSid):" + roomSid);
		Room room = roomService.enter(roomSid, userSid);
		User u = userService.findUserBySid(userSid);
		RespRoom respRoom =  RespRoom.convert(room);
		RoomEnterPush push = new RoomEnterPush();
		RespUser respUser = new RespUser();
		respUser.convert(u);
		push.user = respUser;
		String pushMessage = push.socketResp();
		appendLog("ROOM_ENTER_PUSH:" + pushMessage);
		Set<String> users = room.getUsers();
		for (String roomerSid : users) {
			// 推送通知房间其他用户
			if (!roomerSid.equals(userSid)) {
				String userDesc = userService.findUserBySid(roomerSid).desc();
				User roomUser = userService.findUserBySid(roomerSid);
				RespUser roomRespUser = new RespUser();
				roomRespUser.convert(roomUser);
				respRoom.users.add(roomRespUser);
				if (userService.isOnline(roomerSid)) {
					pushMessage(roomerSid, pushMessage);
					appendLog(String.format("用户%s当前在线, 发送ROOM_ENTER_PUSH", userDesc));
				} else {
					appendLog(String.format("用户%s当前离线, 无法发送ROOM_ENTER_PUSH", userDesc));
				}
			}
		}
		
		RoomEnterOut out = new RoomEnterOut();
		out.room = respRoom;
		return out;
	}
	
	public static class RoomEnterIn {
		public String roomSid = "";
	}
	
	public static class RoomEnterOut extends CommandOut {
		
		public RespRoom room;
		
		public RoomEnterOut() {
			super(Command.ROOM_ENTER);
		}
	}
}
