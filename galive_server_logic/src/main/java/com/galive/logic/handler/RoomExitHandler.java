package com.galive.logic.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.handler.push.RoomExitPush;
import com.galive.logic.handler.push.RoomRefreshPush;
import com.galive.logic.model.Room;

@LogicHandler(desc = "退出房间", command = Command.ROOM_EXIT)
public class RoomExitHandler extends BaseHandler  {

	private static Logger logger = LoggerFactory.getLogger(RoomExitHandler.class);

	@Override
	public CommandOut commandProcess(String userSid, String reqData) {
		logger.debug("退出房间|" + userSid + "|" + reqData);
		Room room = Room.findRoomByUser(userSid);
		if (room != null) {
			room.exitRoom(userSid);
			room.refreshRoomExpireTime();
			// 推送
			RoomExitPush push = new RoomExitPush();
			push.userSid = userSid;
			for (String roomerSid : room.getUsers()) {
				pushMessage(roomerSid, push);
			}
		} 

		// TODO推送客户端更新界面
		RoomRefreshPush push = new RoomRefreshPush();
		pushMessage(null, push);
		
		CommandOut out = new CommandOut(Command.ROOM_EXIT);
		return out;
	}
	
}
