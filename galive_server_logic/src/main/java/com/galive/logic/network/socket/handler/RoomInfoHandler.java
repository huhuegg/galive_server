package com.galive.logic.network.socket.handler;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Room;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.RoomService;
import com.galive.logic.service.RoomService.FindRoomBy;
import com.galive.logic.service.RoomServiceImpl;

@SocketRequestHandler(desc = "房间信息", command = Command.ROOM_INFO)
public class RoomInfoHandler extends SocketBaseHandler {

	private RoomService roomService = new RoomServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--CreateRoomHandler(创建房间)--");
		
		Room room = roomService.findRoom(FindRoomBy.Owner, account);
		if (room != null) {
			RoomInfoOut out = new RoomInfoOut();
			out.room = room;
			return out;
		} else {
			CommandOut out = CommandOut.failureOut(Command.ROOM_INFO, "房间不存在");
			return out;
		}

		
	}
	
	public static class RoomInfoOut extends CommandOut {

		public RoomInfoOut() {
			super(Command.ROOM_INFO);
		}

		public Room room;
	}

}
