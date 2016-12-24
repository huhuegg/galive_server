package com.galive.logic.dao;

import com.alibaba.fastjson.JSON;
import com.galive.logic.db.RedisManager;
import com.galive.logic.model.Room;


public class RoomDaoImpl extends BaseDao implements RoomDao {

	private String roomKey(String roomSid) {
		return RedisManager.getInstance().keyPrefix() + "room:" + roomSid;
	}
	
	private String roomMemberKey(String memberSid) {
		return RedisManager.getInstance().keyPrefix() + "room:member:" + memberSid;
	}
	
	private String roomOwnerKey(String ownerSid) {
		return RedisManager.getInstance().keyPrefix() + "room:owner:" + ownerSid;
	}

	private String roomSidKey() {
		return RedisManager.getInstance().keyPrefix() + "room:incr";
	}
	
	public String generateRoomSid() {
		String key = roomSidKey();
		Long id = jedis().incr(key);
		if (jedis().get(id + "") != null) {
			// 房间已存在
			return generateRoomSid();
		}
		if (id > 999999) {
			// 超过id上限
			jedis().del(key);
			return generateRoomSid();
		}
		return String.valueOf(id);
	}
	
	@Override
	public Room findBySid(String sid) {
		String s = jedis().get(roomKey(sid));
		if (s != null) {
			Room room = JSON.parseObject(s, Room.class);
			return room;
		}
		return null;
	}

	@Override
	public Room findByOwner(String sid) {
		String roomSid = jedis().get(roomOwnerKey(sid));
		if (roomSid != null) {
			Room room = findBySid(roomSid);
			return room;
		}
		return null;
	}

	@Override
	public Room findByMember(String sid) {
		String roomSid = jedis().get(roomMemberKey(sid));
		if (roomSid != null) {
			Room room = findBySid(roomSid);
			return room;
		}
		return null;
	}

	@Override
	public Room save(Room room) {
		if (room.getSid() == null) {
			String sid = generateRoomSid();
			room.setSid(sid);
		}
		String json = JSON.toJSONString(room);
		jedis().set(roomKey(room.getSid()), json);
		
		jedis().set(roomOwnerKey(room.getOwnerSid()), room.getSid());
		for (String member : room.getMembers()) {
			jedis().set(roomMemberKey(member), room.getSid());
		}
		return room;
	}

	@Override
	public void removeMember(String memberSid) {
		jedis().del(roomMemberKey(memberSid));
	}

	@Override
	public void delete(Room room) {
		for (String member : room.getMembers()) {
			jedis().del(roomMemberKey(member));
		}
		jedis().del(roomOwnerKey(room.getOwnerSid()));
		jedis().del(roomKey(room.getSid()));
	}

	
}
