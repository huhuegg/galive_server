package com.galive.logic.network.socket.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Room;
import com.galive.logic.model.User;
import com.galive.logic.network.model.RespUser;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.UserOnlinePush;

@SocketRequestHandler(desc = "客户端上线", command = Command.USR_ONLINE)
public class UserOnlineHandler extends SocketBaseHandler {

	private static Logger logger = LoggerFactory.getLogger(UserOnlineHandler.class);
	
	@Override
	public String handle(String userSid, String reqData) {
		try {
			logger.debug("客户端上线|" + userSid + "|" + reqData);
			Room room = roomService.findRoomByUser(userSid);
			if (room != null) {
				User u = userService.findUserBySid(userSid);
				if (u != null) {
					logger.debug("用户在房间内 ,推送房间其他成员");
					RespUser ru = RespUser.convert(u);
					UserOnlinePush push = new UserOnlinePush();
					push.user = ru;
					String pushMessage = push.socketResp();
					for (String roomUserSid : room.getUsers()) {
						if (!roomUserSid.equals(userSid)) {
							pushMessage(roomUserSid, pushMessage);
						}
					}
				}
			}
			String out = new CommandOut(Command.USR_ONLINE).socketResp();
			logger.debug("客户端上线响应|" + out);
			return out;
		} catch (LogicException e) {
			logger.error(e.getMessage());
			String resp = respFail(e.getMessage());
			return resp;
		} catch (Exception e) {
			logger.error(e.getMessage());
			String resp = respFail(null);
			return resp;
		}
		
	}
	
	private String respFail(String message) {
		String resp = CommandOut.failureOut(Command.USR_ONLINE, message).httpResp();
		logger.error("客户端上线失败|" + resp);
		return resp;
	}
}
