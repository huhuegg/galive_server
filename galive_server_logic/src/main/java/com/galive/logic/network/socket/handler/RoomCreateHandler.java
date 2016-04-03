package com.galive.logic.network.socket.handler;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandIn;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Room;
import com.galive.logic.network.model.RespRoom;
import com.galive.logic.network.socket.SocketRequestHandler;

@SocketRequestHandler(desc = "创建房间", command = Command.ROOM_CREATE)
public class RoomCreateHandler extends SocketBaseHandler  {

	private static Logger logger = LoggerFactory.getLogger(RoomCreateHandler.class);

	@Override
	public String handle(String userSid, String reqData) {
		try {
			logger.debug("创建房间|" + userSid + "|" + reqData);
			EnterRoomRequest in = JSON.parseObject(reqData, EnterRoomRequest.class);
			Room room = roomService.create(in.name, userSid, in.invitedUsers, in.maxUser);

			// TODO 推送邀请人
			
			RoomCreateOut out = new RoomCreateOut();
			out.room = RespRoom.convertFromUserRoom(room);
			String resp = out.socketResp();
			logger.debug("创建房间|" + resp);
			return resp;
		} catch (LogicException e) {
			logger.error(e.getMessage());
			String resp = respFail(e.getMessage());
			return resp;
		} catch (Exception e) {
			logger.error(e.getMessage());
			String resp = respFail(null);
			return resp;
		}
		
	}
	
	public static class EnterRoomRequest extends CommandIn {
		public String name = "";
		public int maxUser = 0;
		public List<String> invitedUsers = new ArrayList<>();
	}
	
	public static class RoomCreateOut extends CommandOut {
		
		public RespRoom room;
		
		public RoomCreateOut() {
			super(Command.ROOM_CREATE);
		}
	}
	
	private String respFail(String message) {
		String resp = CommandOut.failureOut(Command.ROOM_CREATE, message).httpResp();
		logger.error("创建房间失败|" + resp);
		return resp;
	}
	
}
