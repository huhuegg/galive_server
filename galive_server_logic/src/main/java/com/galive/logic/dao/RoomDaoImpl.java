package com.galive.logic.dao;

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

	@Override
	public void saveRoom(String room) {
		jedis.sadd(freeRoomKey(), room);
	}

	@Override
	public String getRandomRoom() {
		String room = jedis.spop(freeRoomKey());
		return room;
	}

	

}
