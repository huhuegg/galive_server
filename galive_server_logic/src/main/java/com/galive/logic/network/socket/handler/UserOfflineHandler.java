package com.galive.logic.network.socket.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.exception.LogicException;
import com.galive.logic.helper.LoggerHelper;
import com.galive.logic.model.Room;
import com.galive.logic.model.User;
import com.galive.logic.network.model.RespUser;
import com.galive.logic.network.socket.ChannelManager;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.UserOfflinePush;
import com.galive.logic.service.LoggerService;
import com.galive.logic.service.LoggerServiceImpl;
import com.galive.logic.service.RoomService;
import com.galive.logic.service.RoomServiceImpl;
import com.galive.logic.service.UserService;
import com.galive.logic.service.UserServiceImpl;

@SocketRequestHandler(desc = "客户端下线", command = Command.USR_OFFLINE)
public class UserOfflineHandler extends SocketBaseHandler {

	private static Logger logger = LoggerFactory.getLogger(UserOfflineHandler.class);
	
	private UserService userService = new UserServiceImpl(logBuffer);
	private RoomService roomService = new RoomServiceImpl(logBuffer);
	private LoggerService loggerService = new LoggerServiceImpl();
	
	@Override
	public String handle(String userSid, String reqData) {
		try {
			logger.debug("客户端下线|" + userSid + "|" + reqData);
			User u = userService.findUserBySid(userSid);
			
			Room room = roomService.findRoomByUser(userSid);
			if (room != null) {
				RespUser ru = new RespUser();
				ru.convert(u);
				UserOfflinePush push = new UserOfflinePush();
				push.user = ru;
				String pushMessage = push.socketResp();
				LoggerHelper.appendLog("用户" + u.desc() + "在房间内, USR_OFFLINE_PUSH:" + pushMessage, logBuffer);
				for (String roomUserSid : room.getUsers()) {
					if (!roomUserSid.equals(userSid)) {
						String userDesc = userService.findUserBySid(roomUserSid).desc();
						if (userService.isOnline(roomUserSid)) {
							pushMessage(roomUserSid, pushMessage);
							LoggerHelper.appendLog(String.format("用户%s当前在线, 发送USR_OFFLINE_PUSH", userDesc), logBuffer);
						} else {
							LoggerHelper.appendLog(String.format("用户%s当前离线, 无法发送USR_OFFLINE_PUSH", userDesc), logBuffer);
						}
					}
				}
			}
			// 移除用户session
			ChannelManager.getInstance().closeAndRemoveChannel(userSid);
			LoggerHelper.appendLog("移除用户session channel", logBuffer);
			return null;
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
	
	private String respFail(String message) {
		String resp = CommandOut.failureOut(Command.USR_ONLINE, message).httpResp();
		return resp;
	}
}
