package com.galive.logic.dao;

import com.galive.logic.dao.cache.RedisManager;

import redis.clients.jedis.Jedis;

public class UserCacheImpl implements UserCache {

	// zadd score:登录时间 val:用户Sid
	private static String userListByLatestLoginKey() {
		return RedisManager.keyPrefix() + "user:list:latest_login";
	}

	@Override
	public void updateLatestLogin(String userSid) {
		// 更新最后登录列表
		String key = userListByLatestLoginKey();
		Jedis j = RedisManager.getResource();
		j.zadd(key, System.currentTimeMillis(), userSid);
		RedisManager.returnToPool(j);
	}

}
