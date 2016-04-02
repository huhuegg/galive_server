package com.galive.logic.network.socket.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Room;
import com.galive.logic.model.User;
import com.galive.logic.network.model.RespUser;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.UserOfflinePush;

@SocketRequestHandler(desc = "客户端下线", command = Command.USR_OFFLINE)
public class UserOfflineHandler extends SocketBaseHandler {

	private static Logger logger = LoggerFactory.getLogger(UserOfflineHandler.class);
	
	@Override
	public CommandOut commandProcess(String userSid, String reqData) {
		logger.debug("客户端下线|" + userSid + "|" + reqData);
		User u = User.findBySid(userSid);
		
		Room room = Room.findRoomByUser(userSid);
		if (room != null) {
			// 用户在房间内 告知房间其他成员
			RespUser ru = RespUser.convertFromUser(u);
			UserOfflinePush push = new UserOfflinePush();
			push.user = ru;
			for (String roomUserSid : room.getUsers()) {
				if (!roomUserSid.equals(userSid)) {
					pushMessage(roomUserSid, push);
				}
			}
		}
		return null;
	}
	
}
