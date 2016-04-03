package com.galive.logic.service;

import java.util.List;

import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Room;

public class RoomServiceImpl implements RoomService {

	@Override
	public Room findRoomByUser(String userSid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Room> list(int index, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Room create(String roomname, String userSid, List<String> invitedUsers, int maxUser) throws LogicException {
//		String name = in.name;
//		if (StringUtils.isBlank(name)) {
//			return CommandOut.failureOut(Command.ROOM_CREATE, "请输入房间名").socketResp();
//		}
//		Room room = Room.createRoom(userSid, name, in.maxUser);
//		room.refreshRoomExpireTime();
		return null;
	}
	
	@Override
	public Room exit(String userSid) throws LogicException {
		// TODO Auto-generated method stub
//		Room room = Room.findRoomByUser(userSid);
//		if (room != null) {
//			room.exitRoom(userSid);
//			room.refreshRoomExpireTime();
//			// 推送
//			RoomExitPush push = new RoomExitPush();
//			push.userSid = userSid;
//			for (String roomerSid : room.getUsers()) {
//				pushMessage(roomerSid, push.socketResp());
//			}
//		} 
		return null;
	}

	@Override
	public Room enter(String roomSid, String userSid) throws LogicException {
//		Room room = Room.findRoom(roomSid);
//		// 房间已销毁
//		if (room == null) {
//			return CommandOut.failureOut(Command.ROOM_ENTER, "房间不存在").socketResp();
//		}
//		// 查询用户是否在房间中
//		Room existRoom = Room.findRoomByUser(userSid);
//		if (existRoom == null) {
//			// 房间已满员
//			if (room.getUsers().size() >= room.getMaxMemberCount()) {
//				return CommandOut.failureOut(Command.ROOM_ENTER, "该房间已满员").socketResp();
//			}
//		} else {
//			// 如果已进入房间，只能进入自己已加入的房间
//			if (!existRoom.getRoomId().equals(room.getRoomId())) {
//				return CommandOut.failureOut(Command.ROOM_ENTER, String.format("您已在其他房间%s中，请先退出该房间。", existRoom.getRoomId())).socketResp();
//			}
//		}
//
//		// 进入房间并刷新房间时间
//		room.enterRoom(userSid);
//		room.refreshRoomExpireTime();
//		
//		User u = User.findBySid(userSid);
//		RoomEnterPush push = new RoomEnterPush();
//		RespUser ru = RespUser.convertFromUser(u);
//		push.user = ru;
//		for (String roomerSid : room.getUsers()) {
//			// 推送通知房间其他用户
//			if (!roomerSid.equals(userSid)) {
//				pushMessage(roomerSid, push.socketResp());
//			}
//		}
		return null;
	}

	

}
