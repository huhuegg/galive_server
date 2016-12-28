package com.galive.logic.network.socket.handler;

import com.alibaba.fastjson.JSON;
import com.galive.logic.network.protocol.Command;
import com.galive.logic.network.protocol.CommandOut;
import com.galive.logic.model.Room;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.EnterRoomHandler.EnterRoomIn;
import com.galive.logic.service.RoomService;
import com.galive.logic.service.RoomService.FindRoomBy;
import com.galive.logic.service.RoomServiceImpl;

@SocketRequestHandler(desc = "房间信息", command = Command.ROOM_INFO)
public class RoomInfoHandler extends SocketBaseHandler {

	private RoomService roomService = new RoomServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--RoomInfoHandler(房间信息)--");
		
		EnterRoomIn in = JSON.parseObject(reqData, EnterRoomIn.class);
		String roomSid = in.roomSid;
		
		Room room = roomService.findRoom(FindRoomBy.id, roomSid);
		if (room != null) {
			RoomInfoOut out = new RoomInfoOut();
			out.room = room;
			return out;
		} else {
			CommandOut out = CommandOut.failureOut(Command.ROOM_INFO, "房间不存在");
			return out;
		}

		
	}
	
	public static class RoomInfoIn {

		public String roomSid = "";
		
	}
	
	public static class RoomInfoOut extends CommandOut {

		public RoomInfoOut() {
			super(Command.ROOM_INFO);
		}

		public Room room;
	}

}
