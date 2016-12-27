package com.galive.logic.dao;

import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.galive.logic.db.RedisManager;
import com.galive.logic.model.Room;

import redis.clients.jedis.Jedis;


public class RoomDaoImpl extends BaseDao implements RoomDao {

	private String roomsKey() {
		return RedisManager.getInstance().keyPrefix() + "room:all";
	}
	
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
		Jedis j = redis.getResource();
		String key = roomSidKey();
		Long id = j.incr(key);
		String idStr = String.format("%06d", id);
		if (j.get(idStr) != null) {
			// 房间已存在
			return generateRoomSid();
		}
		if (id > 999999) {
			// 超过id上限
			j.del(key);
			return generateRoomSid();
		}
		redis.returnToPool(j);
		return idStr;
	}
	
	@Override
	public Room findBySid(String sid) {
		Jedis j = redis.getResource();
		String s = j.get(roomKey(sid));
		if (s != null) {
			Room room = JSON.parseObject(s, Room.class);
			return room;
		}
		redis.returnToPool(j);
		return null;
	}

	@Override
	public Room findByOwner(String sid) {
		Jedis j = redis.getResource();
		String roomSid = j.get(roomOwnerKey(sid));
		if (roomSid != null) {
			Room room = findBySid(roomSid);
			return room;
		}
		redis.returnToPool(j);
		return null;
	}

	@Override
	public Room findByMember(String sid) {
		Jedis j = redis.getResource();
		String roomSid = j.get(roomMemberKey(sid));
		if (roomSid != null) {
			Room room = findBySid(roomSid);
			return room;
		}
		redis.returnToPool(j);
		return null;
	}

	@Override
	public Room save(Room room) {
		Jedis j = redis.getResource();
		if (room.getSid() == null) {
			String sid = generateRoomSid();
			room.setSid(sid);
		}
		String json = JSON.toJSONString(room);
		j.set(roomKey(room.getSid()), json);
		
		j.set(roomOwnerKey(room.getOwnerSid()), room.getSid());
		for (String member : room.getMembers()) {
			j.set(roomMemberKey(member), room.getSid());
		}
		j.sadd(roomsKey(), room.getSid());
		redis.returnToPool(j);
		return room;
	}

	@Override
	public void removeMember(String memberSid) {
		Jedis j = redis.getResource();
		j.del(roomMemberKey(memberSid));
		redis.returnToPool(j);
	}

	@Override
	public void delete(Room room) {
		Jedis j = redis.getResource();
		for (String member : room.getMembers()) {
			j.del(roomMemberKey(member));
		}
		j.del(roomOwnerKey(room.getOwnerSid()));
		j.del(roomKey(room.getSid()));
		j.srem(roomsKey(), room.getSid());
		redis.returnToPool(j);
	}

	@Override
	public Set<String> findAllRooms() {
		Jedis j = redis.getResource();
		Set<String> rooms = j.smembers(roomsKey());
		redis.returnToPool(j);
		return rooms;
	}

	
}
