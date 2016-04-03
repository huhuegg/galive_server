package com.galive.logic.dao.cache;

import org.apache.commons.lang.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisManager {
	
	private static JedisPool pool;
	private static RedisConfig config;
	
	static {
		config = RedisConfig.loadConfig();
		JedisPoolConfig c = new JedisPoolConfig();
		c.setMaxIdle(config.getMaxIdle());
		c.setMaxTotal(config.getMaxTotal());
		c.setMaxWaitMillis(config.getMaxWaitMillis());
		c.setTestWhileIdle(config.isTestWhileIdle());
		c.setTestOnBorrow(config.isTestOnBorrow());
		c.setTestOnReturn(config.isTestOnReturn());
		if (StringUtils.isBlank(config.getAuth())) {
			pool = new JedisPool(c, config.getHost(), config.getPort(), config.getConnectTimeout());
		} else {
			pool = new JedisPool(c, config.getHost(), config.getPort(), config.getConnectTimeout(), config.getAuth());
		}		
	}
	
	public static void returnToPool(Jedis j) {
		//deprecation
//		if (j != null) {
//			pool.returnResource(j);
//		}
	}

	public static Jedis getResource() {
		return pool.getResource();
	}
	
	public static String keyPrefix() {
		return config.getKeyPrefix();
	}
}
