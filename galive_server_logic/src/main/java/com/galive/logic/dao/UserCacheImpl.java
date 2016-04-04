package com.galive.logic.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.dao.cache.RedisManager;
import com.galive.logic.model.User;

import redis.clients.jedis.Jedis;

public class UserCacheImpl implements UserCache {

	private Jedis jedis = RedisManager.getResource();
	private UserDaoImpl userDao = new UserDaoImpl();

	@Override
	protected void finalize() throws Throwable {
		RedisManager.returnToPool(jedis);
		super.finalize();
	}
	
	private String userTokenKey(String userSid) {
		return RedisManager.keyPrefix() + "user:token:" + userSid;
	}

	// zadd score:登录时间 val:用户Sid
	private String userListByLatestLoginKey() {
		return RedisManager.keyPrefix() + "user:list:latest_login";
	}

	private String userDeviceTokenKey(String userSid) {
		Long id = Long.parseLong(userSid);
		return RedisManager.keyPrefix() + "user:device_token:" + (id % 5);
	}

	@Override
	public void updateLatestLogin(String userSid) {
		// 更新最后登录列表
		String key = userListByLatestLoginKey();
		jedis.zadd(key, System.currentTimeMillis(), userSid);
	}
	
	

	@Override
	public List<User> listByLatestLogin(int start, int end) {
		List<User> users = new ArrayList<>();
		String key = userListByLatestLoginKey();
		Set<String> sets = jedis.zrevrange(key, start, end);
		for (String sid : sets) {
			User u = userDao.findUser(sid);
			if (u != null) {
				users.add(u);
			}
		}
		return users;
	}

	@Override
	public void saveDeviceToken(String userSid, String deviceToken) {
		String key = userDeviceTokenKey(userSid);
		jedis.hset(key, userSid, deviceToken);
	}
	
	@Override
	public void deleteDeviceToken(String userSid) {
		String key = userDeviceTokenKey(userSid);
		jedis.hdel(key, userSid);
	}
	
	@Override
	public String findDeviceToken(String userSid) {
		String key = userDeviceTokenKey(userSid);
		String token = jedis.hget(key, userSid);
		return token;
	}

	@Override
	public String findUserToken(String userSid) {
		String key = userTokenKey(userSid);
		String token = jedis.get(key);
		return token;
	}

	@Override
	public void saveUserToken(String userSid, String token) {
		String key = userTokenKey(userSid);
		jedis.set(key, token);
		jedis.expire(key, ApplicationConfig.getInstance().getTokenExpire());
	}

	

	

}
