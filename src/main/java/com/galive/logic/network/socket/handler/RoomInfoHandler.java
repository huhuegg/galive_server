package com.galive.logic.network.socket.handler;

import com.alibaba.fastjson.JSON;
import com.galive.logic.model.Room;
import com.galive.logic.network.protocol.Command;
import com.galive.logic.network.protocol.CommandOut;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.RoomService;
import com.galive.logic.service.RoomService.FindRoomBy;
import com.galive.logic.service.RoomServiceImpl;

import java.util.Map;

@SocketRequestHandler(desc = "房间信息", command = Command.ROOM_INFO)
public class RoomInfoHandler extends WebSocketBaseHandler {

	private RoomService roomService = new RoomServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--RoomInfoHandler(房间信息)--");

		RoomInfoIn in = JSON.parseObject(reqData, RoomInfoIn.class);
		String roomSid = in.roomSid;
		
		Room room = roomService.findRoom(FindRoomBy.id, roomSid);
		if (room != null) {
			if (in.extraInfo != null) {
				room.setExtraInfo(in.extraInfo);
				room = roomService.updateRoomExtraInfo(room, in.extraInfo);
			}
			RoomInfoOut out = new RoomInfoOut();
			out.room = room;
			return out;
		} else {
			return CommandOut.failureOut(Command.ROOM_INFO, "房间不存在");
		}

		
	}
	
	public static class RoomInfoIn {

		public String roomSid = "";
		public Map<String, Object> extraInfo;
		
	}
	
	public static class RoomInfoOut extends CommandOut {

		RoomInfoOut() {
			super(Command.ROOM_INFO);
		}

		public Room room;
	}

}
