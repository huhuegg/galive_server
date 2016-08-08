package com.galive.logic.dao;

import org.apache.commons.lang.time.DateUtils;

import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.dao.cache.RedisManager;
import com.galive.logic.model.Account;

import redis.clients.jedis.Jedis;

public class RoomDaoImpl implements RoomDao {

	private Jedis jedis = RedisManager.getInstance().getResource();

	@Override
	protected void finalize() throws Throwable {
		RedisManager.getInstance().returnToPool(jedis);
		super.finalize();
	}

	

}
