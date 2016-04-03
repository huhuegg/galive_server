package com.galive.logic.network.socket.handler;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.common.protocol.data.PageData;
import com.galive.common.protocol.params.PageParams;
import com.galive.logic.model.Room;
import com.galive.logic.network.model.RespRoom;
import com.galive.logic.network.socket.SocketRequestHandler;

@SocketRequestHandler(desc = "房间列表", command = Command.ROOM_LIST)
public class RoomListHandler extends SocketBaseHandler  {

	private static Logger logger = LoggerFactory.getLogger(RoomListHandler.class);

	@Override
	public String handle(String userSid, String reqData) {
		try {
			logger.debug("获取房间列表|" + userSid + "|" + reqData);
			PageParams in = JSON.parseObject(reqData, PageParams.class);
			
			List<Room> rooms = roomService.list(in.index, in.index + in.size - 1);
			List<RespRoom> respRooms = new ArrayList<>();
			for (Room r : rooms) {
				RespRoom rr = RespRoom.convertFromUserRoom(r);
				respRooms.add(rr);
			}
			PageData<RespRoom> out = new PageData<>(Command.ROOM_LIST, in);
			out.setData(respRooms);
			return out.socketResp();
		} catch (Exception e) {
			logger.error(e.getMessage());
			String resp = respFail(null);
			return resp;
		}	
	}
	
	private String respFail(String message) {
		String resp = CommandOut.failureOut(Command.ROOM_LIST, message).httpResp();
		logger.error("获取房间列表失败|" + resp);
		return resp;
	}
}
