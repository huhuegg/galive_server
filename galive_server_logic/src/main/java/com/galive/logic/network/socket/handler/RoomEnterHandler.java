package com.galive.logic.network.socket.handler;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.exception.LogicException;
import com.galive.logic.helper.LoggerHelper;
import com.galive.logic.model.Room;
import com.galive.logic.model.User;
import com.galive.logic.network.model.RespRoom;
import com.galive.logic.network.model.RespUser;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.RoomEnterPush;
import com.galive.logic.service.LoggerService;
import com.galive.logic.service.LoggerServiceImpl;
import com.galive.logic.service.RoomService;
import com.galive.logic.service.RoomServiceImpl;
import com.galive.logic.service.UserService;
import com.galive.logic.service.UserServiceImpl;

@SocketRequestHandler(desc = "进入房间", command = Command.ROOM_ENTER)
public class RoomEnterHandler extends SocketBaseHandler {

	private static Logger logger = LoggerFactory.getLogger(RoomEnterHandler.class);
	
	private UserService userService = new UserServiceImpl(logBuffer);
	private RoomService roomService = new RoomServiceImpl(logBuffer);
	private LoggerService loggerService = new LoggerServiceImpl();
	
	
	@Override
	public String handle(String userSid, String reqData) {
		try {
			LoggerHelper.appendLog("--进入房间--", logBuffer);
			RoomEnterIn in = JSON.parseObject(reqData, RoomEnterIn.class);
			String roomSid = in.roomSid;

			Room room = roomService.enter(roomSid, userSid);
			User u = userService.findUserBySid(userSid);
			RespRoom respRoom =  RespRoom.convert(room);
			RoomEnterPush push = new RoomEnterPush();
			push.user = RespUser.convert(u);
			String pushMessage = push.socketResp();
			
			LoggerHelper.appendLog("推送房间内其他成员：" + pushMessage, logBuffer);
			Set<String> users = room.getUsers();
			for (String roomerSid : users) {
				// 推送通知房间其他用户
				if (!roomerSid.equals(userSid)) {
					User roomUser = userService.findUserBySid(roomerSid);
					RespUser respUser = RespUser.convert(roomUser);
					respRoom.users.add(respUser);
					if (userService.isOnline(roomerSid)) {
						pushMessage(roomerSid, pushMessage);
						LoggerHelper.appendLog(String.format("用户%s 当前在线, 推送在线消息", roomerSid), logBuffer);
					} else {
						LoggerHelper.appendLog(String.format("用户%s 当前离线。", roomerSid), logBuffer);
					}
				}
			}
			
			RoomEnterOut out = new RoomEnterOut();
			out.room = respRoom;
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
			LoggerHelper.appendLog("响应客户端|" + resp, logBuffer);
			LoggerHelper.appendSplit(logBuffer);
			String logicLog = LoggerHelper.loggerString(logBuffer);
			logger.error(logicLog);
			loggerService.saveLogicLog(logicLog);
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
