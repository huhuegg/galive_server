package com.galive.logic.network.socket.handler;

import java.util.Set;

import com.galive.logic.model.Room;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.ExitRoomPush;
import com.galive.logic.network.protocol.Command;
import com.galive.logic.network.protocol.CommandOut;
import com.galive.logic.service.RoomService;
import com.galive.logic.service.RoomServiceImpl;

@SocketRequestHandler(desc = "离开房间", command = Command.ROOM_EXIT)
public class ExitRoomHandler extends SocketBaseHandler {

	private RoomService roomService = new RoomServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--ExitRoomHandler(离开房间)--");

		Room room = roomService.leaveRoom(account);
		
		if (room != null) {
			ExitRoomPush push = new ExitRoomPush();
			push.accountSid = account;
			String pushContent = push.socketResp();
			Set<String> members = room.getMembers();
			for (String m : members) {
				if (!m.equals(account)) {
					pushMessage(m, pushContent);
					appendLog("推送房间内成员:" + m + " " + pushContent);
				}
			}
		}

		return new CommandOut(Command.ROOM_EXIT);
	}
	
	
}
