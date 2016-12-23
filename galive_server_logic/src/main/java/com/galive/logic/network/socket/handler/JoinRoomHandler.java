package com.galive.logic.network.socket.handler;

import java.util.List;
import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Room;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.JoinRoomPush;
import com.galive.logic.service.RoomService;
import com.galive.logic.service.RoomServiceImpl;

@SocketRequestHandler(desc = "进入房间", command = Command.ROOM_JOIN)
public class JoinRoomHandler extends SocketBaseHandler {

	private RoomService roomService = new RoomServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--JoinRoomHandler(进入房间)--");
		
		JoinRoomIn in = JSON.parseObject(reqData, JoinRoomIn.class);
		String roomSid = in.roomSId;
		appendLog("房间id(roomSid):" + roomSid);
		
		Room room = roomService.joinRoom(roomSid, account);
		
		JoinRoomPush push = new JoinRoomPush();
		push.accountSid = account;
		String pushContent = push.socketResp();
		List<String> members = room.getMembers();
		appendLog("房间内成员数:" + members.size());
		for (String m : members) {
			appendLog("成员id:" + m);
			if (!m.equals(account)) {
				pushMessage(m, pushContent);
				appendLog("推送房间内成员:" + m + " " + pushContent);
			}
		}
		JoinRoomOut out = new JoinRoomOut();
		out.room = room;
		return out;
	}
	
	public static class JoinRoomIn {

		public String roomSId = "";
		
	}
	
	public static class JoinRoomOut extends CommandOut {

		public JoinRoomOut() {
			super(Command.ROOM_JOIN);
		}
		
		public Room room;
		
	}
	
}
