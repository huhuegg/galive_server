package com.galive.logic.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.dao.RoomCache;
import com.galive.logic.dao.RoomCacheImpl;
import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Room;
import com.galive.logic.model.Room.RoomPrivacy;

public class RoomServiceImpl extends BaseService implements RoomService {

	private RoomCache roomCache = new RoomCacheImpl();
	private UserService userService = new UserServiceImpl();


	public RoomServiceImpl() {
		super();
		appendLog("RoomServiceImpl");
	}

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
		List<Room> rooms = new ArrayList<>();
		List<String> roomSids = roomCache.listByCreateTime(index, index + size - 1);
		for (String sid : roomSids) {
			Room room = roomCache.findRoom(sid);
			if (room != null) {
				rooms.add(room);
			}
		}
		return rooms;
	}

	@Override
	public Room create(String roomname, String userSid, String questionSid, List<String> invitees, int maxUser)
			throws LogicException {
		Room existRoom = findRoomByUser(userSid);
		if (existRoom != null) {
			appendLog("用户" + userSid + "已在其他房间" + existRoom.desc() + "中");
			throw new LogicException("已在其他房间中。");
		}
		if (StringUtils.isBlank(roomname)) {
			String error = "房间名不能为空。";
			appendLog(error);
			throw new LogicException(error);
		}
		if (maxUser > ApplicationConfig.getInstance().getLogicConfig().getRoomMaxUser()) {
			String error = "房间人数超过上限。";
			appendLog(error);
			throw new LogicException(error);
		}

		Room room = new Room();
		room.setName(roomname);
		room.setMaxMemberCount(maxUser);
		room.setOwnerId(userSid);
		// 处理被邀请人
		Set<String> roomInvitees = new HashSet<>();
		if (!CollectionUtils.isEmpty(invitees)) {
			appendLog("处理邀请人");
			for (String s : invitees) {
				
				userService.beContact(userSid, s);
				
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
					appendLog("用户" + s + "可被邀请");
					roomInvitees.add(s);
				} else {
					String error = "所邀请的用户正在其他房间中或已被邀请。";
					appendLog(error);
					throw new LogicException(error);
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
		appendLog("用户" + userSid + "绑定房间" + room.desc());
		roomCache.addRoomToUser(roomSid, userSid);

		appendLog("更新房间过期时间");
		roomCache.updateUserInRoomExpire(userSid);
		// 插入列表
		if (room.getPrivacy() == RoomPrivacy.Public) {
			appendLog("插入房间列表");
			roomCache.insertToRoomListByCreateTime(roomSid);
		}
		return room;
	}

	@Override
	public Room enter(String roomSid, String userSid) throws LogicException {
		Room room = roomCache.findRoom(roomSid);
		if (room == null) {
			String error = "房间已过期。";
			appendLog(error);
			throw new LogicException(error);
		}
		Room existRoom = roomCache.findRoomByUser(userSid);
		Room inviteeRoom = roomCache.findRoomByInvitee(userSid);

		// 进入被邀请的房间
		if (inviteeRoom != null && inviteeRoom.getSid().equals(roomSid)) {
			appendLog("进入被邀请房间 " + inviteeRoom.desc());
			if (existRoom != null) {
				// 先退出原有的房间
				appendLog("已在" + existRoom.desc() + "中，退出该房间");
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
					String error = "已在" + existRoom.desc() + "中，退出该房间";
					appendLog(error);
					throw new LogicException(String.format("您已在其他房间%s中，请先退出该房间。", existRoom.desc()));
				}
			}
		}
		Set<String> users = room.getUsers();
		if (users.size() >= room.getMaxMemberCount() && !users.contains(userSid)) {
			String error = "该房间已满员";
			appendLog(error);
			throw new LogicException(error);
		}
		users.add(userSid);
		roomCache.addRoomToUser(roomSid, userSid);
		// 更新房间信息
		room.setUsers(users);
		roomCache.saveRoom(room);
		// 房间已满员 移出列表
		if (users.size() == room.getMaxMemberCount()) {
			appendLog("房间已满员，从房间列表中移出。");
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
		Room room = findRoomByUser(userSid);
		if (room == null) {
			// throw new LogicException("该房间不存在。");
			appendLog("用户当前不在房间中。");
			return null;
		}
		appendLog("清除用户所在房间。");
		roomCache.removeRoomToUser(userSid);
		Set<String> users = room.getUsers();
		users.remove(userSid);
		if (users.isEmpty() || userSid.equals(room.getOwnerId())) {
			// 销毁房间
			appendLog("销毁房间。");
			roomCache.deleteRoom(room);
			// 清除邀请的人
			Set<String> invitees = room.getInvitees();
			for (String s : invitees) {
				roomCache.removeRoomToInvitee(s);
				appendLog("清除被邀请人" + s);
			}
			for (String s : users) {
				roomCache.removeRoomToUser(s);
				appendLog("清除房间内成员" + s);
			}
			roomCache.removeFromListByCreateTime(room.getSid());
		} else {
			room.setUsers(users);
			room = roomCache.saveRoom(room);
			// 更新列表
			if (room.getPrivacy() == RoomPrivacy.Public) {
				appendLog("更新房间列表。");
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
			appendLog("拒绝开房邀请" + inviteeRoom.desc());
		} else {
			appendLog("拒绝开房邀请，该房间不存在。");
		}

		return inviteeRoom;
	}

}
