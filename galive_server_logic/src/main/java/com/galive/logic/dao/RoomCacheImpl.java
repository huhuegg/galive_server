package com.galive.logic.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import com.galive.logic.dao.cache.RedisManager;
import com.galive.logic.model.Room;
import redis.clients.jedis.Jedis;

public class RoomCacheImpl implements RoomCache {

	public static final int ROOM_EXPIRE_INTERVAL = 60 * 60 * 24;
	public static final int ROOM_REFRESH_INTERVAL = 60 * 60;
	public static final int ROOM_INVITEE_TIMEOUT = 60 * 60;
	
	private Jedis jedis = RedisManager.getInstance().getResource();

	@Override
	protected void finalize() throws Throwable {
		RedisManager.getInstance().returnToPool(jedis);
		super.finalize();
	}

	// 记录用户所在的房间，用于重连 set k:[room:user_room:用户id] v:[房间id]
	private String userInRoomKey(String userSid) {
		return RedisManager.getInstance().keyPrefix() + "room:user_room:" + userSid;
	}
	
	// 记录用户被邀请进入的房间，用于自动进入 set k:[room:user_room_invitee:用户id] v:[房间id]
	private String inviteeInRoomKey(String inviteeUserSid) {
		return RedisManager.getInstance().keyPrefix() + "room:user_room_invitee:" + inviteeUserSid;
	}

	// 房间信息 set k:[room:房间id] v:[房间json]
	private String roomKey(String roomId) {
		return RedisManager.getInstance().keyPrefix() + "room:" + roomId;
	}

	// 房间id list
	private String roomSidListKey() {
		return RedisManager.getInstance().keyPrefix() + "room:sid:seq";
	}

	// 房间id set incr
	private String roomSidSeqKey() {
		return RedisManager.getInstance().keyPrefix() + "room:sid:incr";
	}

	// 记录用户所在的房间，用于重连 set k:[room:list:create_time:用户id] v:[房间id]
	private String listByCreateTimeKey() {
		return RedisManager.getInstance().keyPrefix() + "room:list:create_time";
	}
	
	/**
	 * 生成房间id
	 * 
	 * @return
	 */
	private String generateRoomSid() {
		String listKey = roomSidListKey();
		String roomSid = jedis.rpop(listKey);
		if (roomSid == null) {
			long seq = jedis.incr(roomSidSeqKey()) + 1;
			roomSid = seq + "";
			if (seq % 100 == 0) { // 回收房间id
				for (long i = 0; i < seq - 1; i++) {
					if (!jedis.exists(roomKey(i + ""))) {
						jedis.lpush(listKey, i + "");
					}
				}
			}
		}
		String result = String.format("%03d", Long.parseLong(roomSid));
		return result;
	}

	@Override
	public Room saveRoom(Room room) {
		String roomSid = room.getSid();
		if (StringUtils.isBlank(roomSid)) {
			roomSid = generateRoomSid();
		}
		room.setSid(roomSid);
		String json = JSON.toJSONString(room);
		String roomKey = roomKey(roomSid);
		jedis.set(roomKey, json);
		return room;
	}
	
	@Override
	public Room findRoom(String roomSid) {
		String json = jedis.get(roomKey(roomSid));
		Room room = JSON.parseObject(json, Room.class);
		return room;
	}

	@Override
	public Room findRoomByUser(String userSid) {
		String roomSid = jedis.get(userInRoomKey(userSid));
		if (roomSid != null) {
			Room room = findRoom(roomSid);
			return room;
		}
		return null;
	}
	
	@Override
	public Room findRoomByInvitee(String inviteeUserSid) {
		String roomSid = jedis.get(inviteeInRoomKey(inviteeUserSid));
		if (roomSid != null) {
			Room room = findRoom(roomSid);
			return room;
		}
		return null;
	}
	
	@Override
	public void deleteRoom(Room room) {
		String roomSid = room.getSid();
		jedis.del(roomKey(roomSid));
	}
	
	@Override
	public void addRoomToUser(String roomSid, String userSid) {
		String userInRoomKey = userInRoomKey(userSid);
		jedis.set(userInRoomKey, roomSid);
	}
	
	@Override
	public void addRoomToInvitee(String roomSid, String inviteeUserSid) {
		String inviteeRoomKey = inviteeInRoomKey(inviteeUserSid);
		jedis.set(inviteeRoomKey, roomSid);
		jedis.expire(inviteeRoomKey, ROOM_INVITEE_TIMEOUT);
	}
	
	@Override
	public void removeRoomToUser(String userSid) {
		jedis.del(userInRoomKey(userSid));
	}

	@Override
	public void removeRoomToInvitee(String inviteeUserSid) {
		jedis.del(inviteeInRoomKey(inviteeUserSid));
	}
	
	@Override
	public void insertToRoomListByCreateTime(String roomSid) {
		jedis.zadd(listByCreateTimeKey(), System.currentTimeMillis(), roomSid);
	}

	@Override
	public List<Room> listByCreateTime(int start, int end) {
		List<Room> rooms = new ArrayList<Room>();
		String key = listByCreateTimeKey();
		Set<String> sets = jedis.zrevrange(key, start, end);
		for (String sid : sets) {
			Room r = findRoom(sid);
			if (r != null) {
				rooms.add(r);
			}
		}
		return rooms;
	}
	
	@Override
	public void removeFromListByCreateTime(String roomSid) {
		jedis.zrem(listByCreateTimeKey(), roomSid);
	}

	
	
	
	@Override
	public void updateRoomExpire(String roomSid) {
		jedis.expire(roomKey(roomSid), ROOM_REFRESH_INTERVAL);
	}

	@Override
	public void updateUserInRoomExpire(String userSid) {
		jedis.expire(userInRoomKey(userSid), ROOM_REFRESH_INTERVAL);
		
	}

	

	

	

	
	

	

	

}
