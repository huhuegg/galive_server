package com.galive.logic.handler;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandIn;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.handler.push.RoomRefreshPush;
import com.galive.logic.model.Room;
import com.galive.logic.network.model.RespRoom;

@LogicHandler(desc = "创建房间", command = Command.ROOM_CREATE)
public class RoomCreateHandler extends BaseHandler  {

	private static Logger logger = LoggerFactory.getLogger(RoomCreateHandler.class);

	@Override
	public CommandOut commandProcess(String userSid, String reqData) {
		logger.debug("创建房间|" + userSid + "|" + reqData);
		EnterRoomRequest in = JSON.parseObject(reqData, EnterRoomRequest.class);
		String name = in.name;
		if (StringUtils.isBlank(name)) {
			return CommandOut.failureOut(Command.ROOM_CREATE, "请输入房间名");
		}
		Room room = Room.createRoom(userSid, name, in.maxUser);
		room.refreshRoomExpireTime();

		// 推送客户端更新界面
		RoomRefreshPush push = new RoomRefreshPush();
		pushMessage(null, push);
		
		RoomCreateOut out = new RoomCreateOut();
		out.room = RespRoom.convertFromUserRoom(room);
		return out;
	}
	
	public static class EnterRoomRequest extends CommandIn {
		public String name = "";
		public short maxUser = 0;
	}
	
	public static class RoomCreateOut extends CommandOut {
		
		public RespRoom room;
		
		public RoomCreateOut() {
			super(Command.ROOM_CREATE);
		}
	}

	
}
