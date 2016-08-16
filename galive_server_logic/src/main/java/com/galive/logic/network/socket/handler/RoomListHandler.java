package com.galive.logic.network.socket.handler;

import java.util.List;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.RoomService;
import com.galive.logic.service.RoomServiceImpl;

@SocketRequestHandler(desc = "房间列表", command = Command.ROOM_LIST)
public class RoomListHandler extends SocketBaseHandler {

	private RoomService roomService = new RoomServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--RoomListHandler(房间列表)--");
		List<String> rooms = roomService.listUsedRoom();
		RoomListOut out = new RoomListOut();
		out.rooms = rooms;
		return out;
	}

	public static class RoomListOut extends CommandOut {

		public RoomListOut() {
			super(Command.ROOM_LIST);
		}

		public List<String> rooms;
	}

}
