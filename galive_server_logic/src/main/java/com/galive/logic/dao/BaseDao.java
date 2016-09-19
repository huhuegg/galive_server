package com.galive.logic.dao;

import com.galive.logic.dao.cache.RedisManager;

import redis.clients.jedis.Jedis;

public class BaseDao {

	private Jedis jedis;
	
	protected Jedis jedis() {
		if (jedis == null) {
			jedis = RedisManager.getInstance().getResource();
		}
		return jedis;
	}

	@Override
	protected void finalize() throws Throwable {
		if (jedis != null) {
			RedisManager.getInstance().returnToPool(jedis);
		}
		super.finalize();
	}
	
}
