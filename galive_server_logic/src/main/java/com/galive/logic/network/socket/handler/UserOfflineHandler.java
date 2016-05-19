package com.galive.logic.network.socket.handler;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Room;
import com.galive.logic.model.User;
import com.galive.logic.network.model.RespUser;
import com.galive.logic.network.socket.ChannelManager;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.UserOfflinePush;
import com.galive.logic.service.RoomService;
import com.galive.logic.service.RoomServiceImpl;
import com.galive.logic.service.UserService;
import com.galive.logic.service.UserServiceImpl;

@SocketRequestHandler(desc = "客户端下线", command = Command.USR_OFFLINE)
public class UserOfflineHandler extends SocketBaseHandler {

	private UserService userService = new UserServiceImpl();
	private RoomService roomService = new RoomServiceImpl();
	
	@Override
	public CommandOut handle(String userSid, String reqData) throws Exception {
		appendLog("--UserOfflineHandler(客户端下线)--");
		
		User u = userService.findUserBySid(userSid);
		
//		appendLog("用户:" + u.desc());
		Room room = roomService.findRoomByUser(userSid);
		if (room != null) {
			roomService.exit(userSid);
			RespUser ru = new RespUser();
			ru.convert(u);
			UserOfflinePush push = new UserOfflinePush();
			push.user = ru;
			String pushMessage = push.socketResp();
//			appendLog("用户" + u.desc() + "在房间内, USR_OFFLINE_PUSH:" + pushMessage);
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
		
		// 移除用户session
		ChannelManager.getInstance().closeAndRemoveChannel(userSid);
		appendLog("移除用户session channel");
		return null;
		
	}
	
}
