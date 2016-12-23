package com.galive.logic.network.socket.handler;

import java.util.List;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Room;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.DestroyRoomPush;
import com.galive.logic.service.RoomService;
import com.galive.logic.service.RoomService.FindRoomBy;
import com.galive.logic.service.RoomServiceImpl;

@SocketRequestHandler(desc = "销毁房间", command = Command.ROOM_DESTROY)
public class DestroyRoomHandler extends SocketBaseHandler {

	private RoomService roomService = new RoomServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--DestroyRoomHandler(销毁房间)--");
		
		Room room = roomService.findRoom(FindRoomBy.Owner, account);
		if (room != null) {
			List<String> members = room.getMembers();
			DestroyRoomPush push = new DestroyRoomPush();
			String pushContent = push.socketResp();
			for (String member : members) {
				if (!member.equals(account)) {
					roomService.leaveRoom(member);
					pushMessage(member, pushContent);
					appendLog("推送房间内成员:" + member + " " + pushContent);
				}
			}
			roomService.destroyRoom(account);
		}
		
		
		CommandOut out = new CommandOut(Command.ROOM_DESTROY);
		return out;
	}
	
}
