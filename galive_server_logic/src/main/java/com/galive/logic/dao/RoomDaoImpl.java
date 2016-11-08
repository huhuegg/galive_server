package com.galive.logic.dao;

import java.util.Set;

import com.galive.logic.dao.cache.RedisManager;

public class RoomDaoImpl extends BaseDao implements RoomDao {

	private String freeRoomKey() {
		return RedisManager.getInstance().keyPrefix() + "room:free";
	}
	
	private String usedRoomKey() {
		return RedisManager.getInstance().keyPrefix() + "room:used";
	}

	@Override
	public void deleteUseds() {
		String key = usedRoomKey();
		Set<String> rooms = jedis().smembers(key);
		for (String r : rooms) {
			jedis().srem(key, r);
		}
	}

	@Override
	public void deleteUsed(String room) {
		jedis().srem(usedRoomKey(), room);
	}

	@Override
	public void deleteFrees() {
		String key = freeRoomKey();
		Set<String> rooms = jedis().smembers(key);
		for (String r : rooms) {
			jedis().srem(key, r);
		}
	}

	@Override
	public void saveFree(String room) {
		jedis().sadd(freeRoomKey(), room);
	}

	@Override
	public void saveUsed(String room) {
		jedis().sadd(usedRoomKey(), room);
	}

	@Override
	public boolean roomUsed(String room) {
		boolean used = jedis().sismember(usedRoomKey(), room);
		return used;
	}

	@Override
	public String popFree() {
		String room = jedis().spop(freeRoomKey());
		return room;
	}

	@Override
	public Set<String> findUseds() {
		Set<String> usedsRooms = jedis().smembers(usedRoomKey());
		return usedsRooms;
	}
	
}
