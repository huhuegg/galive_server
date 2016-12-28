package com.galive.logic.network.socket.handler;

import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.galive.logic.model.Room;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.EnterRoomPush;
import com.galive.logic.network.protocol.Command;
import com.galive.logic.network.protocol.CommandOut;
import com.galive.logic.service.RoomService;
import com.galive.logic.service.RoomServiceImpl;

@SocketRequestHandler(desc = "进入房间", command = Command.ROOM_ENTER)
public class EnterRoomHandler extends SocketBaseHandler {

	private RoomService roomService = new RoomServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--JoinRoomHandler(进入房间)--");
		
		EnterRoomIn in = JSON.parseObject(reqData, EnterRoomIn.class);
		String roomSid = in.roomSid;
		appendLog("房间id(roomSid):" + roomSid);
		
		Room room = roomService.joinRoom(roomSid, account);

		EnterRoomPush push = new EnterRoomPush();
		push.accountSid = account;
		String pushContent = push.socketResp();
		Set<String> members = room.getMembers();
		appendLog("房间内成员数:" + members.size());
		for (String m : members) {
			appendLog("成员id:" + m);
			if (!m.equals(account)) {
				pushMessage(m, pushContent);
				appendLog("推送房间内成员:" + m + " " + pushContent);
			}
		}
		EnterRoomOut out = new EnterRoomOut();
		out.room = room;
		return out;
	}
	
	public static class EnterRoomIn {

		String roomSid = "";
		
	}
	
	public static class EnterRoomOut extends CommandOut {

		EnterRoomOut() {
			super(Command.ROOM_ENTER);
		}
		
		public Room room;
		
	}
	
}
