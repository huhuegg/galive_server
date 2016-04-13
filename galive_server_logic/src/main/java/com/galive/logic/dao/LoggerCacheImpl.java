package com.galive.logic.dao;

import com.galive.logic.dao.cache.RedisManager;

import redis.clients.jedis.Jedis;

public class LoggerCacheImpl implements LoggerCache {

	private static final long MAX_LEN = 100;
	private Jedis jedis = RedisManager.getInstance().getResource();
	
	@Override
	protected void finalize() throws Throwable {
		RedisManager.getInstance().returnToPool(jedis);
		super.finalize();
	}
	
	private String logicLogKey() {
		return RedisManager.getInstance().keyPrefix() + "log:logic";
	}

	@Override
	public void saveLogicLog(String log) {
		String key = logicLogKey();
		long now = System.currentTimeMillis();
		jedis.zadd(key, now, log);
		long count = jedis.zcount(key, 0, -1);
		if (count >= MAX_LEN * 10) {
			jedis.zremrangeByRank(key, 0, count - MAX_LEN);
		}
	}

}
