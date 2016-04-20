package com.galive.logic.network.socket.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.exception.LogicException;
import com.galive.logic.helper.LoggerHelper;
import com.galive.logic.model.Live;
import com.galive.logic.model.Room;
import com.galive.logic.model.User;
import com.galive.logic.network.model.RespLive;
import com.galive.logic.network.model.RespUser;
import com.galive.logic.network.socket.ChannelManager;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.LiveLeavePush;
import com.galive.logic.network.socket.handler.push.LiveStopPush;
import com.galive.logic.network.socket.handler.push.UserOfflinePush;
import com.galive.logic.service.LiveService;
import com.galive.logic.service.LiveServiceImpl;
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
	private LiveService liveService = new LiveServiceImpl(logBuffer);
	private LoggerService loggerService = new LoggerServiceImpl();
	
	@Override
	public String handle(String userSid, String reqData) {
		try {
			LoggerHelper.appendLog("客户端下线|" + userSid + "|" + reqData, logBuffer);
			
			User u = userService.findUserBySid(userSid);
			LoggerHelper.appendLog("用户:" + u.desc(), logBuffer);
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
			
			Live live = liveService.findLiveByUser(userSid);
			if (live != null) {
				liveService.stopLive(userSid);
				LoggerHelper.appendLog("结束直播:" + live.getName(), logBuffer);
				// 推送
				List<String> audienceSids = liveService.listAllAudiences(live.getSid());
				LiveStopPush push = new LiveStopPush();
				RespLive respLive = new RespLive();
				respLive.convert(live);
				push.live = respLive;
				String pushMessage = push.socketResp();
				LoggerHelper.appendLog("LIVE_STOP_PUSH:" + pushMessage, logBuffer);
				LoggerHelper.appendLog("推送用户数量:" + audienceSids.size(), logBuffer);
				for (String sid : audienceSids) {
					if (!sid.equals(userSid)) {
						if (userService.isOnline(sid)) {
							pushMessage(sid, pushMessage);
						}
					}
				}
			}
			
			// 退出观看
			live = liveService.findLiveByAudience(userSid);
			if (live != null) {
				liveService.leaveLive(userSid);
				// 推送
				LoggerHelper.appendLog("退出正在观看的直播:" + live.getName(), logBuffer);
				List<String> audienceSids = liveService.listAllAudiences(live.getSid());
				LiveLeavePush push = new LiveLeavePush();
				RespUser respUser = new RespUser();
				User user = userService.findUserBySid(userSid);
				respUser.convert(user);
				push.user = respUser;
				String pushMessage = push.socketResp();
				LoggerHelper.appendLog("LIVE_LEAVE_PUSH:" + pushMessage, logBuffer);
				LoggerHelper.appendLog("推送用户数量:" + audienceSids.size(), logBuffer);
				for (String sid : audienceSids) {
					if (!sid.equals(userSid)) {
						if (userService.isOnline(sid)) {
							pushMessage(sid, pushMessage);
						}
					}
				}
			}
			
			// 移除用户session
			ChannelManager.getInstance().closeAndRemoveChannel(userSid);
			LoggerHelper.appendLog("移除用户session channel", logBuffer);
			String logicLog = LoggerHelper.loggerString(logBuffer);
			logger.info(logicLog);
			loggerService.saveLogicLog(logicLog);
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
