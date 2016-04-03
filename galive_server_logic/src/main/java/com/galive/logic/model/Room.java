package com.galive.logic.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.dao.cache.RedisManager;

import redis.clients.jedis.Jedis;

public class Room {

	static {
		clear(null);
	}
	
	public static final int ROOM_EXPIRE_INTERVAL = 60 * 60 * 24;
	public static final int ROOM_REFRESH_INTERVAL = 60 * 60;
	
	private String roomId = "";
	
	private String ownerId = "";
	
	private String name = "";
	
	private short maxMemberCount = 0;
	
	private Set<String> users = new HashSet<>();
	
	public static Room createRoom(String userSid, String name, short maxUser) {
		Room room = null;
		Jedis j = null;
		try {
			room = new Room();
			String roomId = generateRoomSid();
			room.name = name;
			room.roomId = roomId;
			room.ownerId = userSid;
			room.setMaxMemberCount((short)Math.max(ApplicationConfig.getInstance().getLogicConfig().getRoomMaxUser(), maxUser));
			room.users.add(userSid);
			String json = JSON.toJSONString(room);
			j = RedisManager.getResource();
			// 房间信息
			String roomKey = roomKey(roomId);
			j.set(roomKey, json);
			// 用户对应的房间
			String userInRoomKey = userInRoomKey(userSid);
			j.set(userInRoomKey, roomId);
			// 加入房间列表
			j.zadd(listByCreateTimeKey(), System.currentTimeMillis(), roomId);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisManager.returnToPool(j);
		}
		return room;
	}
	
	public static Room findRoom(String roomSid) {
		Jedis j = RedisManager.getResource();
		Room room = null;
		try {
			String json = j.get(roomKey(roomSid));
			room = JSON.parseObject(json, Room.class);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisManager.returnToPool(j);
		}
		return room;
	}
	
	public static Room findRoomByUser(String userSid) {
		Jedis j = RedisManager.getResource();
		try {
			String roomSid = j.get(userInRoomKey(userSid));
			if (roomSid != null) {
				Room room = findRoom(roomSid);
				return room;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisManager.returnToPool(j);
		}
		return null;
	}
	
	public void enterRoom(String userSid) {
		Jedis j = null;
		try {
			users.add(userSid);
			j = RedisManager.getResource();
			String userInRoomKey = userInRoomKey(userSid);
			j.set(userInRoomKey, roomId);
			
			// 房间信息
			String roomKey = roomKey(roomId);
			String json = JSON.toJSONString(this);
			j.set(roomKey, json);
			// 房间已满员 移出列表
			if (users.size() == maxMemberCount) {
				j.zrem(listByCreateTimeKey(), roomId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisManager.returnToPool(j);
		}
	}
	
	public void exitRoom(String userSid) {
		Jedis j = null;
		try {
			users.remove(userSid);
			j = RedisManager.getResource();
			if (users.isEmpty() || userSid.equals(ownerId)) {
				// 销毁房间
				j.del(roomKey(roomId));
				j.zrem(listByCreateTimeKey(), roomId);
				for (String s : users) {
					j.del(userInRoomKey(s));
				}
			} else {
				String json = JSON.toJSONString(this);
				String roomKey = roomKey(roomId);
				j.set(roomKey, json);
				// 加入列表
				j.zadd(listByCreateTimeKey(), System.currentTimeMillis(), roomId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisManager.returnToPool(j);
		}
	}
	
	public void refreshRoomExpireTime() {
		Jedis j = null;
		try {
			j = RedisManager.getResource();
			for (String userSid : users) {
				j.expire(userInRoomKey(userSid), ROOM_REFRESH_INTERVAL);
			}
			j.expire(roomKey(roomId), ROOM_REFRESH_INTERVAL);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisManager.returnToPool(j);
		}	
	}
	
	public static List<Room> listRooms(int startIndex, int endIndex) {
		Jedis j = null;
		List<Room> rooms = new ArrayList<Room>();
		try {
			j = RedisManager.getResource();
			String key = listByCreateTimeKey();
			Set<String> sets = j.zrevrange(key, startIndex, endIndex);
			for (String sid : sets) {
				Room r = findRoom(sid);
				if (r != null) {
					rooms.add(r);
				} 
			}
			return rooms;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisManager.returnToPool(j);
		}
		return rooms;
	}
	
	/**
	 * 生成房间id
	 * @return
	 */
	private static String generateRoomSid() {
		Jedis j = null;
		String roomSid = "";
		try {
			j = RedisManager.getResource();
			String listKey = roomSidListKey();
			roomSid = j.rpop(listKey);
			if (roomSid == null) {
				long seq = j.incr(roomSidSeqKey());
				roomSid = seq + "";
				if (seq % 100 == 0) { // 回收房间id
					for (long i = 0; i < seq - 1; i++) {
						if (!j.exists(roomKey(i + ""))) {
							j.lpush(listKey, i + "");
						}
					}
				}
			}
			return String.format("%03d", Long.parseLong(roomSid));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisManager.returnToPool(j);
		}
		return "";
	}
	
	/**
	 * 清除用户相关的房间信息
	 * @param userSid null则清除所有房间信息
	 */
	public static void clear(String userSid) {
		Jedis j = null;
		try {
			j = RedisManager.getResource();
			if (userSid == null) {
				String listKey = listByCreateTimeKey();
				Set<String> ids = j.zrevrange(listKey, 0, -1);
				for (String s : ids) {
					Room r = findRoom(s);
					if (r != null) {
						for (String uid : r.users) {
							j.del(userInRoomKey(uid));
						}
					}
					j.del(roomKey(s));
				}
				j.zremrangeByRank(listKey, 0, -1);
				j.del(roomSidListKey());
				j.del(roomSidSeqKey());
			} else {
				// TODO 暂时清理列表
				String roomSid = userInRoomKey(userSid);
				if (roomSid != null) {
					String listKey = listByCreateTimeKey();
					j.zrem(listKey, roomSid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisManager.returnToPool(j);
		}
		
	}
	
	// 房间id set incr
	private static String roomSidSeqKey() {
		return RedisManager.keyPrefix() + "room:sid:incr";
	}
	// 房间id list
	private static String roomSidListKey() {
		return RedisManager.keyPrefix() + "room:sid:seq";
	}
	
	// 房间信息 set k:[room:房间id] v:[房间json]
	private static String roomKey(String roomId) {
		return RedisManager.keyPrefix() + "room:" + roomId;
	}
	
	// 记录用户所在的房间，用于重连 set k:[user_in_room:用户id] v:[房间id]
	private static String userInRoomKey(String userSid) {
		return RedisManager.keyPrefix() + "user_in_room:" + userSid;
	}
	
	// 记录用户所在的房间，用于重连 set k:[user_in_room:用户id] v:[房间id]
	private static String listByCreateTimeKey() {
		return RedisManager.keyPrefix() + "room:list:create_time";
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<String> getUsers() {
		return users;
	}

	public void setUsers(Set<String> users) {
		this.users = users;
	}

	public short getMaxMemberCount() {
		return maxMemberCount;
	}

	public void setMaxMemberCount(short maxMemberCount) {
		this.maxMemberCount = maxMemberCount;
	}


	
}
