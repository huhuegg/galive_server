package com.galive.logic.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.dao.RoomCacheImpl;
import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Room;

public class RoomServiceImpl implements RoomService {

	private RoomCacheImpl roomCache = new RoomCacheImpl();

	@Override
	public Room findRoomByUser(String userSid) {
		Room room = roomCache.findRoomByUser(userSid);
		return room;
	}

	@Override
	public List<Room> listByCreateTime(int index, int size) {
		List<Room> rooms = roomCache.listByCreateTime(index, index + size - 1);
		return rooms;
	}

	@Override
	public Room create(String roomname, String userSid, List<String> invitedUsers, int maxUser) throws LogicException {
		if (StringUtils.isBlank(roomname)) {
			throw new LogicException("房间名不能为空。");
		}
		if (maxUser > ApplicationConfig.getInstance().getLogicConfig().getRoomMaxUser()) {
			throw new LogicException("房间人数超过上限");
		}

		Room room = new Room();
		room.setName(roomname);
		room.setMaxMemberCount(maxUser);
		room.setOwnerId(userSid);
		Set<String> users = new HashSet<>();
		users.add(userSid);
		for (String s : invitedUsers) {
			users.add(s);
		}
		room.setUsers(users);
		// 保存房间信息
		room = roomCache.saveRoom(room);
		String roomSid = room.getRoomId();
		// 更新房间过期时间
		roomCache.updateRoomExpire(roomSid);
		// 用户绑定房间
		for (String s : users) {
			roomCache.bindRoomToUser(roomSid, s);
			// 设置用户在房间中的过期时间
			roomCache.updateUserInRoomExpire(s);
		}
		roomCache.insertToRoomListByCreateTime(roomSid);
		return room;
	}

	@Override
	public Room exit(String userSid) throws LogicException {
		Room room = roomCache.findRoomByUser(userSid);
		if (room == null) {
//			throw new LogicException("该房间不存在。");
			return null;
		} 
		Set<String> users = room.getUsers();
		users.remove(userSid);
		if (users.isEmpty() || userSid.equals(room.getOwnerId())) {
			// 销毁房间
			roomCache.deleteRoom(room);
			for (String s : users) {
				roomCache.unbindRoomToUser(s);
			}
			roomCache.removeFromListByCreateTime(room.getRoomId());
			return null;
		} else {
			room.setUsers(users);
			room = roomCache.saveRoom(room);
			// 更新列表
			roomCache.insertToRoomListByCreateTime(room.getRoomId());
		}
		return room;
	}

	@Override
	public Room enter(String roomSid, String userSid) throws LogicException {
		Room room = roomCache.findRoom(roomSid);
		if (room == null) {
			throw new LogicException("该房间不存在。");
		}
		Room existRoom = roomCache.findRoomByUser(userSid);
		if (existRoom == null) {
			Set<String> users = room.getUsers();
			if (users.size() >= room.getMaxMemberCount()) {
				throw new LogicException("该房间已满员。");
			}
			users.add(userSid);
			roomCache.bindRoomToUser(roomSid, userSid);
			// 更新房间信息
			room.setUsers(users);
			roomCache.saveRoom(room);
			// 房间已满员 移出列表
			if (users.size() == room.getMaxMemberCount()) {
				roomCache.removeFromListByCreateTime(roomSid);
			}
		} else {
			if (!existRoom.getRoomId().equals(roomSid)) {
				throw new LogicException(String.format("您已在其他房间%s中，请先退出该房间。", existRoom.getRoomId()));
			}
		}
		// 更新过期时间
		roomCache.updateRoomExpire(roomSid);
		Set<String> users = room.getUsers();
		for (String s : users) {
			roomCache.updateUserInRoomExpire(s);
		}
		return room;
	}

}
