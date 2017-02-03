package com.galive.logic.dao;

import com.alibaba.fastjson.JSON;
import com.galive.logic.db.RedisManager;
import com.galive.logic.model.Room;
import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.HashSet;
import java.util.Set;


public class CourseDaoImpl extends BaseDao implements CourseDao {

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

    private String generateRoomSid() {
        Jedis jedis = null;
        String idStr = "";
        try {
            jedis = redis.getResource();
            String key = roomSidKey();
            Long id = jedis.incr(key);
            idStr = String.format("%06d", id);
            if (jedis.get(idStr) != null) {
                // 房间已存在
                return generateRoomSid();
            }
            if (id > 999999) {
                // 超过id上限
                jedis.del(key);
                return generateRoomSid();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                redis.returnToPool(jedis);
            }
        }
        return idStr;
    }

    @Override
    public Room findBySid(String sid) {
        Jedis jedis = null;
        Room room = null;
        try {
            jedis = redis.getResource();
            String s = jedis.get(roomKey(sid));
            if (!StringUtils.isEmpty(s)) {
                room = JSON.parseObject(s, Room.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                redis.returnToPool(jedis);
            }
        }
        return room;
    }

    @Override
    public Room findByOwner(String sid) {
        Jedis jedis = null;
        Room room = null;
        try {
            jedis = redis.getResource();
            String roomSid = jedis.get(roomOwnerKey(sid));
            if (!StringUtils.isEmpty(roomSid)) {
                room = findBySid(roomSid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                redis.returnToPool(jedis);
            }
        }
        return room;
    }

    @Override
    public Room findByMember(String sid) {
        Jedis jedis = null;
        Room room = null;
        try {
            jedis = redis.getResource();
            String roomSid = jedis.get(roomMemberKey(sid));
            if (!StringUtils.isEmpty(roomSid)) {
                room = findBySid(roomSid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                redis.returnToPool(jedis);
            }
        }
        return room;
    }

    @Override
    public Room save(Room room) {
        Jedis jedis = null;
        try {
            jedis = redis.getResource();
            if (StringUtils.isEmpty(room.getSid())) {
                String sid = generateRoomSid();
                room.setSid(sid);
            }
            String json = JSON.toJSONString(room);
            jedis.set(roomKey(room.getSid()), json);

            jedis.set(roomOwnerKey(room.getOwnerSid()), room.getSid());
            for (String member : room.getMembers()) {
                jedis.set(roomMemberKey(member), room.getSid());
            }
            jedis.sadd(roomsKey(), room.getSid());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                redis.returnToPool(jedis);
            }
        }
        return room;
    }

    @Override
    public void removeMember(String memberSid) {
        Jedis jedis = null;
        try {
            jedis = redis.getResource();
            jedis.del(roomMemberKey(memberSid));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                redis.returnToPool(jedis);
            }
        }
    }

    @Override
    public void delete(Room room) {
        Jedis jedis = null;
        try {
            jedis = redis.getResource();
            for (String member : room.getMembers()) {
                jedis.del(roomMemberKey(member));
            }
            jedis.del(roomOwnerKey(room.getOwnerSid()));
            jedis.del(roomKey(room.getSid()));
            jedis.srem(roomsKey(), room.getSid());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                redis.returnToPool(jedis);
            }
        }
    }

    @Override
    public Set<String> findAllRooms() {
        Jedis jedis = null;
        Set<String> rooms = new HashSet<>();
        try {
            jedis = redis.getResource();
            rooms = jedis.smembers(roomsKey());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                redis.returnToPool(jedis);
            }
        }
        return rooms;
    }


}
