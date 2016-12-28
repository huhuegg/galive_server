package com.galive.logic.network.socket.handler;

import com.galive.logic.model.Room;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.protocol.Command;
import com.galive.logic.network.protocol.CommandOut;
import com.galive.logic.service.RoomService;
import com.galive.logic.service.RoomServiceImpl;

@SocketRequestHandler(desc = "创建房间", command = Command.ROOM_CREATE)
public class CreateRoomHandler extends SocketBaseHandler {

	private RoomService roomService = new RoomServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--CreateRoomHandler(创建房间)--");
		
		Room room = roomService.createRoom(account);
	
		CreateRoomOut out = new CreateRoomOut();
		out.room = room;
		return out;
	}
	
	public static class CreateRoomOut extends CommandOut {

		CreateRoomOut() {
			super(Command.ROOM_CREATE);
		}

		public Room room;
	}

}
