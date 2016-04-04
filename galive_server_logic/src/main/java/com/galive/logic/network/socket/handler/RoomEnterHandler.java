package com.galive.logic.network.socket.handler;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Room;
import com.galive.logic.model.User;
import com.galive.logic.network.model.RespRoom;
import com.galive.logic.network.model.RespUser;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.RoomEnterPush;

@SocketRequestHandler(desc = "进入房间", command = Command.ROOM_ENTER)
public class RoomEnterHandler extends SocketBaseHandler {

	private static Logger logger = LoggerFactory.getLogger(RoomEnterHandler.class);
	
	@Override
	public String handle(String userSid, String reqData) {
		try {
			logger.debug("进入房间|" + userSid + "|" + reqData);
			RoomEnterIn in = JSON.parseObject(reqData, RoomEnterIn.class);
			String roomSid = in.roomSid;

			Room room = roomService.enter(roomSid, userSid);
			User u = userService.findUserBySid(userSid);
			
			RoomEnterPush push = new RoomEnterPush();
			push.user = RespUser.convert(u);
			String pushMessage = push.socketResp();
			logger.debug("进入房间|推送其他用户：" + pushMessage);
			Set<String> users = room.getUsers();
			for (String roomerSid : users) {
				// 推送通知房间其他用户
				if (!roomerSid.equals(userSid)) {
					pushMessage(roomerSid, pushMessage);
				}
			}
			
			RoomEnterOut out = new RoomEnterOut();
			String resp = out.socketResp();
			logger.debug("进入房间|" + resp);
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
	
	public static class RoomEnterIn {
		public String roomSid = "";
	}
	
	public static class RoomEnterOut extends CommandOut {
		
		public RespRoom room;
		
		public RoomEnterOut() {
			super(Command.ROOM_ENTER);
		}
	}
	
	private String respFail(String message) {
		String resp = CommandOut.failureOut(Command.ROOM_ENTER, message).httpResp();
		logger.error("进入房间失败|" + resp);
		return resp;
	}
}
