package com.galive.logic.dao;

import java.util.Set;

import com.galive.logic.dao.cache.RedisManager;
import redis.clients.jedis.Jedis;

public class RoomDaoImpl implements RoomDao {

	private Jedis jedis = RedisManager.getInstance().getResource();

	
	@Override
	protected void finalize() throws Throwable {
		RedisManager.getInstance().returnToPool(jedis);
		super.finalize();
	}
	
	private String freeRoomKey() {
		return RedisManager.getInstance().keyPrefix() + "room:free";
	}
	
	private String usedRoomKey() {
		return RedisManager.getInstance().keyPrefix() + "room:used";
	}

	@Override
	public void removeFreeRooms() {
		String key = freeRoomKey();
		Set<String> rooms = jedis.smembers(key);
		for (String r : rooms) {
			jedis.srem(key, r);
		}
		
	}
	
	@Override
	public void saveFreeRoom(String room) {
		jedis.sadd(freeRoomKey(), room);
	}

	@Override
	public void saveUsedRoom(String room) {
		jedis.sadd(usedRoomKey(), room);
	}

	@Override
	public boolean roomUsed(String room) {
		boolean used = jedis.sismember(usedRoomKey(), room);
		return used;
	}

	@Override
	public String popFreeRoom() {
		String room = jedis.spop(freeRoomKey());
		return room;
	}

	@Override
	public void removeUsedRooms() {
		String key = usedRoomKey();
		Set<String> rooms = jedis.smembers(key);
		for (String r : rooms) {
			jedis.srem(key, r);
		}
	}

	@Override
	public Set<String> findUsedRooms() {
		Set<String> usedsRooms = jedis.smembers(usedRoomKey());
		return usedsRooms;
	}

	@Override
	public void removeUsedRoom(String room) {
		jedis.srem(usedRoomKey(), room);
	}

	

	

}
