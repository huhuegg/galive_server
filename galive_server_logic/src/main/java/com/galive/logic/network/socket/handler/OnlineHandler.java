package com.galive.logic.network.socket.handler;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Room;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.RoomService;
import com.galive.logic.service.RoomService.FindRoomBy;
import com.galive.logic.service.RoomServiceImpl;

@SocketRequestHandler(desc = "用户上线", command = Command.ONLINE)
public class OnlineHandler extends SocketBaseHandler {

	private RoomService roomService = new RoomServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--OnlineHandler(用户上线)--");

		OnlineOut out = new OnlineOut();
		// 返回房间信息
		Room room = roomService.findRoom(FindRoomBy.Owner, account);
		if (room == null) {
			room = roomService.findRoom(FindRoomBy.Member, account);
		}
		out.room = room;
		return out;
	}
	
	public class OnlineOut extends CommandOut {

		public Room room;
		
		public OnlineOut() {
			super(Command.ONLINE);
			
		}	
	}
}
