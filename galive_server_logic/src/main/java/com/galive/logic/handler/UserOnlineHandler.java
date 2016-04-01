package com.galive.logic.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.handler.model.RespUser;
import com.galive.logic.handler.push.UserOnlinePush;
import com.galive.logic.model.Room;
import com.galive.logic.model.User;

@LogicHandler(desc = "客户端上线", command = Command.USR_ONLINE)
public class UserOnlineHandler extends BaseHandler {

	private static Logger logger = LoggerFactory.getLogger(UserOnlineHandler.class);
	
	@Override
	public CommandOut commandProcess(String userSid, String reqData) {
		logger.debug("客户端上线|" + userSid + "|" + reqData);
		
		User u = User.findBySid(userSid);
		
		Room room = Room.findRoomByUser(userSid);
		if (room != null) {
			// 用户在房间内 告知房间其他成员
			RespUser ru = RespUser.convertFromUser(u);
			UserOnlinePush push = new UserOnlinePush();
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
