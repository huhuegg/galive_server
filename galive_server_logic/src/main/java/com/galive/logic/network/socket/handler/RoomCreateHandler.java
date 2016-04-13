package com.galive.logic.network.socket.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.ApplicationMain;
import com.galive.logic.ApplicationMain.ApplicationMode;
import com.galive.logic.exception.LogicException;
import com.galive.logic.helper.APNSHelper;
import com.galive.logic.helper.LoggerHelper;
import com.galive.logic.model.Room;
import com.galive.logic.model.User;
import com.galive.logic.network.model.RespRoom;
import com.galive.logic.network.model.RespUser;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.RoomInviteePush;
import com.galive.logic.service.LoggerService;
import com.galive.logic.service.LoggerServiceImpl;
import com.galive.logic.service.RoomService;
import com.galive.logic.service.RoomServiceImpl;
import com.galive.logic.service.UserService;
import com.galive.logic.service.UserServiceImpl;

@SocketRequestHandler(desc = "创建房间", command = Command.ROOM_CREATE)
public class RoomCreateHandler extends SocketBaseHandler  {

	private static Logger logger = LoggerFactory.getLogger(RoomCreateHandler.class);

	private UserService userService = new UserServiceImpl(logBuffer);
	private RoomService roomService = new RoomServiceImpl(logBuffer);
	private LoggerService loggerService = new LoggerServiceImpl();
	
	@Override
	public String handle(String userSid, String reqData) {
		try {
			LoggerHelper.appendLog("--创建房间--", logBuffer);
			EnterRoomRequest in = JSON.parseObject(reqData, EnterRoomRequest.class);
			Room room = roomService.create(in.name, userSid, in.invitees, in.maxUser);
			
			Set<String> invitees = room.getInvitees();
			if (!CollectionUtils.isEmpty(invitees)) {
				User invitor = userService.findUserBySid(userSid);
				
				RoomInviteePush inviteePush = new RoomInviteePush();
				RespRoom inviteeRoom = RespRoom.convert(room);
				inviteeRoom.invitor = RespUser.convert(invitor);
				inviteePush.room = inviteeRoom;
				String pushMessage = inviteePush.socketResp();
				LoggerHelper.appendLog("推送邀请人：" + pushMessage, logBuffer);
				
				List<String> deviceTokens = new ArrayList<>();
				for (String invitee : invitees) {
					if (userService.isOnline(invitee)) {
						pushMessage(invitee, pushMessage);
						LoggerHelper.appendLog(String.format("用户%s 当前在线, 推送在线消息", invitee), logBuffer);
					} else {
						LoggerHelper.appendLog(String.format("用户%s 当前离线。", invitee), logBuffer);
					}
					String token = userService.findDeviceToken(invitee);
					if (!StringUtils.isBlank(token)) {
						LoggerHelper.appendLog(String.format("用户%s device_token(%s) 发送苹果推送。", invitee, token), logBuffer);
						deviceTokens.add(token);
					} else {
						LoggerHelper.appendLog(String.format("用户%s device_token(%s) 无效。", invitee, token), logBuffer);
					}
				}
				// 苹果推送
				if (!CollectionUtils.isEmpty(deviceTokens)) {
					APNSHelper apns = new APNSHelper(ApplicationMain.get().getMode() == ApplicationMode.Distribution);
					String content = String.format("%s邀请你加入Ta的房间。", invitor.getNickname());
					apns.push(deviceTokens, content);
					LoggerHelper.appendLog(String.format("推送信息:" + content), logBuffer);
				}
			}
			
			RoomCreateOut out = new RoomCreateOut();
			out.room = RespRoom.convert(room);
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
	
	public static class EnterRoomRequest {
		public String name = "";
		public int maxUser = 0;
		public List<String> invitees = new ArrayList<>();
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
