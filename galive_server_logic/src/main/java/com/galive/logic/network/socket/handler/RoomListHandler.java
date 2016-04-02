package com.galive.logic.network.socket.handler;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.common.protocol.PageCommandIn;
import com.galive.common.protocol.PageCommandOut;
import com.galive.logic.model.Room;
import com.galive.logic.network.model.RespRoom;
import com.galive.logic.network.socket.SocketRequestHandler;

@SocketRequestHandler(desc = "房间列表", command = Command.ROOM_LIST)
public class RoomListHandler extends SocketBaseHandler  {

	private static Logger logger = LoggerFactory.getLogger(RoomListHandler.class);

	@Override
	public CommandOut commandProcess(String userSid, String reqData) {
		logger.debug("房间列表|" + userSid + "|" + reqData);
		PageCommandIn in = JSON.parseObject(reqData, PageCommandIn.class);
		
		List<Room> rooms = Room.listRooms(in.index, in.index + in.size - 1);
		List<RespRoom> respRooms = new ArrayList<>();
		for (Room r : rooms) {
			RespRoom rr = RespRoom.convertFromUserRoom(r);
			respRooms.add(rr);
		}
		PageCommandOut<RespRoom> out = new PageCommandOut<>(Command.ROOM_LIST, in);
		out.setData(respRooms);
		return out;
	}
}
