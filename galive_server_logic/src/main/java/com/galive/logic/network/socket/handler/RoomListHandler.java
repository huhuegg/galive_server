package com.galive.logic.network.socket.handler;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.common.protocol.PageCommandIn;
import com.galive.common.protocol.PageCommandOut;
import com.galive.logic.model.Room;
import com.galive.logic.network.model.RespRoom;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.RoomService;
import com.galive.logic.service.RoomServiceImpl;

@SocketRequestHandler(desc = "房间列表", command = Command.ROOM_LIST)
public class RoomListHandler extends SocketBaseHandler  {

	private RoomService roomService = new RoomServiceImpl();
	
	@Override
	public CommandOut handle(String userSid, String reqData) throws Exception {
		appendLog("--RoomListHandler(获取房间列表)--");
		PageCommandIn in = JSON.parseObject(reqData, PageCommandIn.class);
		
		int index = in.index;
		int size = in.size; 
		appendLog("起始游标(index):" + index);
		appendLog("分页数量(size):" + size);
		
		List<Room> rooms = roomService.listByCreateTime(index, size);
		List<RespRoom> respRooms = new ArrayList<>();
		for (Room r : rooms) {
			RespRoom rr = RespRoom.convert(r);
			respRooms.add(rr);
		}
		PageCommandOut<RespRoom> out = new PageCommandOut<>(Command.ROOM_LIST, in);
		out.setData(respRooms);
		return out;
	}
}
