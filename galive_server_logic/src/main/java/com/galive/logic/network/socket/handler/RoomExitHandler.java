package com.galive.logic.network.socket.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Room;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.RoomExitPush;

@SocketRequestHandler(desc = "退出房间", command = Command.ROOM_EXIT)
public class RoomExitHandler extends SocketBaseHandler  {

	private static Logger logger = LoggerFactory.getLogger(RoomExitHandler.class);

	@Override
	public String handle(String userSid, String reqData) {
		logger.debug("退出房间|" + userSid + "|" + reqData);
		Room room = Room.findRoomByUser(userSid);
		if (room != null) {
			room.exitRoom(userSid);
			room.refreshRoomExpireTime();
			// 推送
			RoomExitPush push = new RoomExitPush();
			push.userSid = userSid;
			for (String roomerSid : room.getUsers()) {
				pushMessage(roomerSid, push.socketResp());
			}
		} 
		
		CommandOut out = new CommandOut(Command.ROOM_EXIT);
		return out.socketResp();
	}
	
}
