package com.galive.logic.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.dao.RoomCacheImpl;
import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Room;
import com.galive.logic.model.Room.RoomPrivacy;

public class RoomServiceImpl implements RoomService {

	private RoomCacheImpl roomCache = new RoomCacheImpl();
	private UserServiceImpl userService = new UserServiceImpl();

	@Override
	public Room findRoomByUser(String userSid) {
		Room room = roomCache.findRoomByUser(userSid);
		return room;
	}
	
	@Override
	public Room findRoomByInvitee(String inviteeUserSid) {
		Room room = roomCache.findRoomByInvitee(inviteeUserSid);
		return room;
	}

	@Override
	public List<Room> listByCreateTime(int index, int size) {
		List<Room> rooms = roomCache.listByCreateTime(index, index + size - 1);
		return rooms;
	}

	@Override
	public Room create(String roomname, String userSid, List<String> invitees, int maxUser) throws LogicException {
		Room existRoom = roomCache.findRoomByUser(userSid);
		if (existRoom != null) {
			throw new LogicException("已在其他房间中。");
		}
		if (StringUtils.isBlank(roomname)) {
			throw new LogicException("房间名不能为空。");
		}
		if (maxUser > ApplicationConfig.getInstance().getLogicConfig().getRoomMaxUser()) {
			throw new LogicException("房间人数超过上限。");
		}
		
		Room room = new Room();
		room.setName(roomname);
		room.setMaxMemberCount(maxUser);
		room.setOwnerId(userSid);
		// 处理被邀请人
		Set<String> roomInvitees = new HashSet<>();
		if (!CollectionUtils.isEmpty(invitees)) {
			for (String s : invitees) {
				boolean online = userService.isOnline(s);
				Room inRoom = roomCache.findRoomByUser(s);
				Room inviteeRoom = roomCache.findRoomByInvitee(s);
				boolean canBeInvite = false;
				if (online) {
					// 目标在线时仅能邀请不在房间中与未被邀请的人
					if (inRoom == null && inviteeRoom == null) {
						canBeInvite = true;
					} 
				} else {
					// 目标离线时未被邀请的人
					if (inviteeRoom == null) {
						canBeInvite = true;
					} 
				}
				if (canBeInvite) {
					roomInvitees.add(s);
				} else {
					throw new LogicException("所邀请的用户正在其他房间中或已被邀请"); 
				}
				
			}
			room.setPrivacy(RoomPrivacy.Privacy);
		} else {
			room.setPrivacy(RoomPrivacy.Public);
		}
		room.setInvitees(roomInvitees);

		Set<String> roomUsers = new HashSet<>();
		roomUsers.add(userSid);
		room.setUsers(roomUsers);
		
		// 保存房间信息
		room = roomCache.saveRoom(room);
		String roomSid = room.getSid();
		for (String s : roomInvitees) {
			// 绑定被邀请的房间
			roomCache.addRoomToInvitee(roomSid, s);
		}
		
		
		// 更新房间过期时间
		roomCache.updateRoomExpire(roomSid);
		// 用户绑定房间
		roomCache.addRoomToUser(roomSid, userSid);
		roomCache.updateUserInRoomExpire(userSid);
		// 插入列表
		if (room.getPrivacy() == RoomPrivacy.Public) {
			roomCache.insertToRoomListByCreateTime(roomSid);
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
		Room inviteeRoom = roomCache.findRoomByInvitee(userSid);
		
		// 进入被邀请的房间
		if (inviteeRoom != null && inviteeRoom.getSid().equals(roomSid)) {
			if (existRoom != null) {
				// 先退出原有的房间
				exit(userSid);
			}
			// 将用户与邀请房间解绑
			roomCache.removeRoomToInvitee(userSid);
			Set<String> invitees = inviteeRoom.getInvitees();
			invitees.remove(userSid);
			inviteeRoom.setInvitees(invitees);
			roomCache.saveRoom(inviteeRoom);
		} else {
			if (existRoom != null) {
				if (!existRoom.getSid().equals(roomSid)) {
					throw new LogicException(String.format("您已在其他房间%s中，请先退出该房间。", existRoom.getSid()));
				}
			}
		}
		Set<String> users = room.getUsers();
		if (users.size() >= room.getMaxMemberCount() && !users.contains(userSid)) {
			throw new LogicException("该房间已满员。");
		}
		users.add(userSid);
		roomCache.addRoomToUser(roomSid, userSid);
		// 更新房间信息
		room.setUsers(users);
		roomCache.saveRoom(room);
		// 房间已满员 移出列表
		if (users.size() == room.getMaxMemberCount()) {
			roomCache.removeFromListByCreateTime(roomSid);
		}
		// 更新过期时间
		roomCache.updateRoomExpire(roomSid);
		for (String s : users) {
			roomCache.updateUserInRoomExpire(s);
		}
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
			roomCache.removeRoomToUser(userSid);
			// 清除邀请的人
			Set<String> invitees = room.getInvitees();
			for (String s : invitees) {
				roomCache.removeRoomToInvitee(s);
			}
			for (String s : users) {
				roomCache.removeRoomToUser(s);
			}
			roomCache.removeFromListByCreateTime(room.getSid());
		} else {
			room.setUsers(users);
			room = roomCache.saveRoom(room);
			// 更新列表
			if (room.getPrivacy() == RoomPrivacy.Public) {
				roomCache.insertToRoomListByCreateTime(room.getSid());
			}
		}
		return room;
	}

	@Override
	public Room refuseInvite(String userSid) throws LogicException {
		Room inviteeRoom = findRoomByInvitee(userSid);
		if (inviteeRoom != null) {
			roomCache.removeRoomToInvitee(userSid);
			Set<String> invitees = inviteeRoom.getInvitees();
			invitees.remove(inviteeRoom.getSid());
			inviteeRoom.setInvitees(invitees);
			inviteeRoom = roomCache.saveRoom(inviteeRoom);
		} 
		return inviteeRoom;
	}

	

	

}
