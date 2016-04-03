package com.galive.logic.network.socket.handler;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Room;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.RoomExitPush;

@SocketRequestHandler(desc = "退出房间", command = Command.ROOM_EXIT)
public class RoomExitHandler extends SocketBaseHandler  {

	private static Logger logger = LoggerFactory.getLogger(RoomExitHandler.class);

	@Override
	public String handle(String userSid, String reqData) {
		try {
			logger.debug("退出房间|" + userSid + "|" + reqData);
			Room room = roomService.exit(userSid);
			if (room != null) {
				// 推送
				RoomExitPush push = new RoomExitPush();
				push.userSid = userSid;
				String pushMessage = push.socketResp();
				logger.debug("退出房间|推送其他用户：" + pushMessage);
				Set<String> users = room.getUsers();
				for (String roomerSid : users) {
					if (!roomerSid.equals(userSid)) {
						pushMessage(roomerSid, pushMessage);
					}
				}
			}
			
			CommandOut out = new CommandOut(Command.ROOM_EXIT);
			String resp = out.socketResp();
			logger.debug("退出房间|" + resp);
			return resp;
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
		String resp = CommandOut.failureOut(Command.ROOM_EXIT, message).httpResp();
		logger.error("退出房间失败|" + resp);
		return resp;
	}
}
