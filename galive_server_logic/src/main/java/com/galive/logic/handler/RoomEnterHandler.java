package com.galive.logic.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandIn;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.handler.push.RoomEnterPush;
import com.galive.logic.handler.push.RoomRefreshPush;
import com.galive.logic.model.Room;
import com.galive.logic.model.User;
import com.galive.logic.network.model.RespRoom;
import com.galive.logic.network.model.RespUser;

@LogicHandler(desc = "进入房间", command = Command.ROOM_ENTER)
public class RoomEnterHandler extends BaseHandler {

	private static Logger logger = LoggerFactory.getLogger(RoomEnterHandler.class);
	
	@Override
	public CommandOut commandProcess(String userSid, String reqData) {
		logger.debug("进入房间|" + userSid + "|" + reqData);
		RoomEnterIn in = JSON.parseObject(reqData, RoomEnterIn.class);
		String roomSid = in.roomSid;
		Room room = Room.findRoom(roomSid);
		// 房间已销毁
		if (room == null) {
			return CommandOut.failureOut(Command.ROOM_ENTER, "房间不存在");
		}
		// 查询用户是否在房间中
		Room existRoom = Room.findRoomByUser(userSid);
		if (existRoom == null) {
			// 房间已满员
			if (room.getUsers().size() >= room.getMaxMemberCount()) {
				return CommandOut.failureOut(Command.ROOM_ENTER, "该房间已满员");
			}
		} else {
			// 如果已进入房间，只能进入自己已加入的房间
			if (!existRoom.getRoomId().equals(room.getRoomId())) {
				return CommandOut.failureOut(Command.ROOM_ENTER, String.format("您已在其他房间%s中，请先退出该房间。", existRoom.getRoomId()));
			}
		}

		// 进入房间并刷新房间时间
		room.enterRoom(userSid);
		room.refreshRoomExpireTime();
		
		User u = User.findBySid(userSid);
		RoomEnterPush push = new RoomEnterPush();
		RespUser ru = RespUser.convertFromUser(u);
		push.user = ru;
		for (String roomerSid : room.getUsers()) {
			// 推送通知房间其他用户
			if (!roomerSid.equals(userSid)) {
				pushMessage(roomerSid, push);
			}
		}
		
		// TODO 推送客户端更新界面
		RoomRefreshPush refreshPush = new RoomRefreshPush();
		pushMessage(null, refreshPush);
		
		RoomEnterOut out = new RoomEnterOut();
		out.room = RespRoom.convertFromUserRoom(room);
		return out;
	}
	
	public static class RoomEnterIn extends CommandIn {
		public String roomSid = "";
	}
	
	public static class RoomEnterOut extends CommandOut {
		
		public RespRoom room;
		
		public RoomEnterOut() {
			super(Command.ROOM_ENTER);
		}
	}
	
}
