package com.galive.logic.network.socket.handler;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandIn;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.exception.LogicException;
import com.galive.logic.helper.LoggerHelper;
import com.galive.logic.model.Room;
import com.galive.logic.model.User;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.RoomExitPush;
import com.galive.logic.service.LoggerService;
import com.galive.logic.service.LoggerServiceImpl;
import com.galive.logic.service.RoomService;
import com.galive.logic.service.RoomServiceImpl;
import com.galive.logic.service.UserService;
import com.galive.logic.service.UserServiceImpl;

@SocketRequestHandler(desc = "退出房间", command = Command.ROOM_EXIT)
public class RoomExitHandler extends SocketBaseHandler  {

	private static Logger logger = LoggerFactory.getLogger(RoomExitHandler.class);

	private UserService userService = new UserServiceImpl(logBuffer);
	private RoomService roomService = new RoomServiceImpl(logBuffer);
	private LoggerService loggerService = new LoggerServiceImpl();
	
	@Override
	public String handle(String userSid, String reqData) {
		try {
			LoggerHelper.appendLog("--退出房间--", logBuffer);
			RoomExitIn in = JSON.parseObject(reqData, RoomExitIn.class);
			String roomSid = in.roomSid;
			
			Room inviteeRoom = roomService.findRoomByInvitee(userSid);
			if (inviteeRoom != null && inviteeRoom.getSid().equals(roomSid)) {
				// 拒绝进入房间
				Room room = roomService.refuseInvite(userSid);
				// 推送邀请人
				User invitor = userService.findUserBySid(room.getOwnerId());
				RoomExitPush push = new RoomExitPush();
				push.userSid = userSid;
				push.refuseInvite = true;
				String pushMessage = push.socketResp();
				LoggerHelper.appendLog(String.format("拒绝房间(%s)邀请,推送房主%s", inviteeRoom.getSid(), invitor.desc() + pushMessage), logBuffer);
				pushMessage(invitor.getSid(), pushMessage);
			} else {
				Room room = roomService.exit(userSid);
				if (room != null) {
					// 推送
					RoomExitPush push = new RoomExitPush();
					push.userSid = userSid;
					String pushMessage = push.socketResp();
					LoggerHelper.appendLog("通知房间内其他成员:" + pushMessage, logBuffer);
					
					logger.debug("退出房间|推送其他用户：" + pushMessage);
					Set<String> users = room.getUsers();
					for (String roomerSid : users) {
						if (!roomerSid.equals(userSid)) {
							if (userService.isOnline(roomerSid)) {
								pushMessage(roomerSid, pushMessage);
								LoggerHelper.appendLog(String.format("用户%s 当前在线, 推送在线消息", roomerSid), logBuffer);
							} else {
								LoggerHelper.appendLog(String.format("用户%s 当前离线。", roomerSid), logBuffer);
							}
						}
					}
				}
			}
			CommandOut out = new CommandOut(Command.ROOM_EXIT);
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
	
	public static class RoomExitIn extends CommandIn {
		public String roomSid;
	}
	
	private String respFail(String message) {
		String resp = CommandOut.failureOut(Command.ROOM_EXIT, message).httpResp();
		logger.error("退出房间失败|" + resp);
		return resp;
	}
}
