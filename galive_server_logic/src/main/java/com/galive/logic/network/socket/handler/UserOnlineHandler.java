package com.galive.logic.network.socket.handler;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Room;
import com.galive.logic.model.User;
import com.galive.logic.network.model.RespRoom;
import com.galive.logic.network.model.RespUser;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.UserOnlinePush;

@SocketRequestHandler(desc = "客户端上线", command = Command.USR_ONLINE)
public class UserOnlineHandler extends SocketBaseHandler {

	private static Logger logger = LoggerFactory.getLogger(UserOnlineHandler.class);
	
	@Override
	public String handle(String userSid, String reqData) {
		try {
			logger.debug("客户端上线|" + userSid + "|" + reqData);
			
			UserOnlineOut out = new UserOnlineOut();
			
			Room room = roomService.findRoomByUser(userSid);
			if (room != null) {
				User u = userService.findUserBySid(userSid);
				RespRoom respRoom = RespRoom.convert(room);
				RespUser ru = RespUser.convert(u);
				UserOnlinePush push = new UserOnlinePush();
				push.user = ru;
				String pushMessage = push.socketResp();
				logger.debug("用户在房间内 ,推送房间其他成员:" + pushMessage);
				Set<String> roomUsers = room.getUsers();
				for (String roomUserSid : roomUsers) {
					if (!roomUserSid.equals(userSid)) {
						pushMessage(roomUserSid, pushMessage);
						
						User roomUser = userService.findUserBySid(roomUserSid);
						RespUser roomRespUser = RespUser.convert(roomUser);
						respRoom.users.add(roomRespUser);
					}
				}				
				out.room = respRoom;
			}
			

			Room inviteeRoom = roomService.findRoomByInvitee(userSid);
			if (inviteeRoom != null) {
				RespRoom respRoom = RespRoom.convert(inviteeRoom);
				respRoom.invitor = RespUser.convert(userService.findUserBySid(inviteeRoom.getOwnerId()));
				out.inviteeRoom = respRoom;
			}
			
			String resp = out.socketResp();
			logger.debug("客户端上线响应|" + resp);
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
	
	public static class UserOnlineOut extends CommandOut {

		public UserOnlineOut() {
			super(Command.USR_ONLINE);
		}

		public RespRoom room;
		public RespRoom inviteeRoom;
	}
	
	private String respFail(String message) {
		String resp = CommandOut.failureOut(Command.USR_ONLINE, message).httpResp();
		logger.error("客户端上线失败|" + resp);
		return resp;
	}
}
