package com.galive.logic.network.socket.handler;

import java.util.List;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
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
import com.galive.logic.service.RoomService;
import com.galive.logic.service.RoomServiceImpl;
import com.galive.logic.service.UserService;
import com.galive.logic.service.UserServiceImpl;

@SocketRequestHandler(desc = "客户端下线", command = Command.USR_OFFLINE)
public class UserOfflineHandler extends SocketBaseHandler {

	private UserService userService = new UserServiceImpl();
	private RoomService roomService = new RoomServiceImpl();
	private LiveService liveService = new LiveServiceImpl();
	
	@Override
	public CommandOut handle(String userSid, String reqData) throws Exception {
		appendLog("--UserOfflineHandler(客户端下线)--");
		
		User u = userService.findUserBySid(userSid);
		appendLog("用户:" + u.desc());
		Room room = roomService.findRoomByUser(userSid);
		if (room != null) {
			RespUser ru = new RespUser();
			ru.convert(u);
			UserOfflinePush push = new UserOfflinePush();
			push.user = ru;
			String pushMessage = push.socketResp();
			appendLog("用户" + u.desc() + "在房间内, USR_OFFLINE_PUSH:" + pushMessage);
			for (String roomUserSid : room.getUsers()) {
				if (!roomUserSid.equals(userSid)) {
					String userDesc = userService.findUserBySid(roomUserSid).desc();
					if (userService.isOnline(roomUserSid)) {
						pushMessage(roomUserSid, pushMessage);
						appendLog(String.format("用户%s当前在线, 发送USR_OFFLINE_PUSH", userDesc));
					} else {
						appendLog(String.format("用户%s当前离线, 无法发送USR_OFFLINE_PUSH", userDesc));
					}
				}
			}
		}
		
		Live live = liveService.findLiveByUser(userSid);
		if (live != null) {
			liveService.stopLive(userSid);
			appendLog(String.format("结束直播:" + live.getName()));
			// 推送
			List<String> audienceSids = liveService.listAllAudiences(live.getSid());
			LiveStopPush push = new LiveStopPush();
			RespLive respLive = new RespLive();
			respLive.convert(live);
			push.live = respLive;
			String pushMessage = push.socketResp();
			appendLog("LIVE_STOP_PUSH:" + pushMessage);
			appendLog("推送用户数量:" + audienceSids.size());
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
			appendLog("退出正在观看的直播:" + live.getName());
			List<String> audienceSids = liveService.listAllAudiences(live.getSid());
			LiveLeavePush push = new LiveLeavePush();
			RespUser respUser = new RespUser();
			User user = userService.findUserBySid(userSid);
			respUser.convert(user);
			push.user = respUser;
			String pushMessage = push.socketResp();
			appendLog("LIVE_LEAVE_PUSH:" + pushMessage);
			appendLog("推送用户数量:" + audienceSids.size());
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
		appendLog("移除用户session channel");
		return null;
		
	}
	
}
