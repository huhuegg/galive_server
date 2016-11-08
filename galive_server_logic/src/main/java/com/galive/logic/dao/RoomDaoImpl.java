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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteUsed(String room) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteFrees() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveFree(String room) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveUsed(String room) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean roomUsed(String room) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String useFree() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> findUseds() {
		// TODO Auto-generated method stub
		return null;
	}

	/*@Override
	public void removeFreeRooms() {
		String key = freeRoomKey();
		Set<String> rooms = jedis().smembers(key);
		for (String r : rooms) {
			jedis().srem(key, r);
		}
		
	}
	
	@Override
	public void saveFreeRoom(String room) {
		jedis().sadd(freeRoomKey(), room);
	}

	@Override
	public void saveUsedRoom(String room) {
		jedis().sadd(usedRoomKey(), room);
	}

	@Override
	public boolean roomUsed(String room) {
		boolean used = jedis().sismember(usedRoomKey(), room);
		return used;
	}

	@Override
	public String popFreeRoom() {
		String room = jedis().spop(freeRoomKey());
		return room;
	}

	@Override
	public void removeUsedRooms() {
		String key = usedRoomKey();
		Set<String> rooms = jedis().smembers(key);
		for (String r : rooms) {
			jedis().srem(key, r);
		}
	}

	@Override
	public Set<String> findUsedRooms() {
		Set<String> usedsRooms = jedis().smembers(usedRoomKey());
		return usedsRooms;
	}

	@Override
	public void removeUsedRoom(String room) {
		jedis().srem(usedRoomKey(), room);
	}

	*/

	

}
