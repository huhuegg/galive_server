package com.galive.logic.network.socket.handler;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.exception.LogicException;
import com.galive.logic.helper.LoggerHelper;
import com.galive.logic.model.Room;
import com.galive.logic.model.User;
import com.galive.logic.network.model.RespRoom;
import com.galive.logic.network.model.RespUser;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.UserOnlinePush;
import com.galive.logic.service.LoggerService;
import com.galive.logic.service.LoggerServiceImpl;
import com.galive.logic.service.RoomService;
import com.galive.logic.service.RoomServiceImpl;
import com.galive.logic.service.UserService;
import com.galive.logic.service.UserServiceImpl;

@SocketRequestHandler(desc = "客户端上线", command = Command.USR_ONLINE)
public class UserOnlineHandler extends SocketBaseHandler {

	private static Logger logger = LoggerFactory.getLogger(UserOnlineHandler.class);
	
	private UserService userService = new UserServiceImpl(logBuffer);
	private RoomService roomService = new RoomServiceImpl(logBuffer);
	private LoggerService loggerService = new LoggerServiceImpl();
	
	@Override
	public String handle(String userSid, String reqData) {
		try {
			LoggerHelper.appendLog("--用户上线--", logBuffer);
			
			UserOnlineOut out = new UserOnlineOut();
			
			Room room = roomService.findRoomByUser(userSid);
			if (room != null) {
				User u = userService.findUserBySid(userSid);
				RespRoom respRoom = RespRoom.convert(room);
				RespUser ru = new RespUser();
				ru.convert(u);
				UserOnlinePush push = new UserOnlinePush();
				push.user = ru;
				String pushMessage = push.socketResp();
				LoggerHelper.appendLog(String.format("用户%s在房间" + room.desc() + "内 ,USR_ONLINE_PUSH:%s", u.desc(), pushMessage), logBuffer);
				Set<String> roomUsers = room.getUsers();
				for (String roomUserSid : roomUsers) {
					if (!roomUserSid.equals(userSid)) {
						String userDesc = userService.findUserBySid(roomUserSid).desc();
						if (userService.isOnline(roomUserSid)) {
							pushMessage(roomUserSid, pushMessage);
							LoggerHelper.appendLog(String.format("用户%s当前在线, 发送USR_ONLINE_PUSH", userDesc), logBuffer);
						} else {
							LoggerHelper.appendLog(String.format("用户%s当前离线, 无法发送USR_ONLINE_PUSH", userDesc), logBuffer);
						}
						User roomUser = userService.findUserBySid(roomUserSid);
						RespUser roomRespUser = new RespUser();
						roomRespUser.convert(roomUser);
						respRoom.users.add(roomRespUser);
					}
				}				
				out.room = respRoom;
			}
			

			Room inviteeRoom = roomService.findRoomByInvitee(userSid);
			if (inviteeRoom != null) {
				RespRoom respRoom = RespRoom.convert(inviteeRoom);
				RespUser invitor = new RespUser();
				invitor.convert(userService.findUserBySid(inviteeRoom.getOwnerId()));
				respRoom.invitor = invitor;
				out.inviteeRoom = respRoom;
			}
			
			String resp = out.socketResp();
			LoggerHelper.appendLog("响应客户端|" + resp, logBuffer);
			LoggerHelper.appendSplit(logBuffer);
			String logicLog = LoggerHelper.loggerString(logBuffer);
			logger.info(logicLog);
			loggerService.saveLogicLog(logicLog);
			return resp;
		} catch (LogicException e) {
			String resp = respFail(e.getMessage());
			LoggerHelper.appendLog("响应客户端|" + resp, logBuffer);
			LoggerHelper.appendSplit(logBuffer);
			String logicLog = LoggerHelper.loggerString(logBuffer);
			logger.error(logicLog);
			loggerService.saveLogicLog(logicLog);
			return resp;
		} catch (Exception e) {
			String resp = respFail(null);
			LoggerHelper.appendLog("发生错误" + e.getMessage(), logBuffer);
			LoggerHelper.appendLog("响应客户端|" + resp, logBuffer);
			LoggerHelper.appendSplit(logBuffer);
			String logicLog = LoggerHelper.loggerString(logBuffer);
			logger.error(logicLog);
			loggerService.saveLogicLog(logicLog);
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
		return resp;
	}
}
