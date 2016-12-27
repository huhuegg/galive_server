package com.galive.logic.db;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisManager {

	private static Logger logger = LoggerFactory.getLogger(RedisManager.class);
	
	private static RedisManager instance = null;
	private static JedisPool pool;
	private static RedisConfig config;

	private RedisManager() {
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

	public final static RedisManager getInstance() {
		if (instance == null) {
			synchronized (RedisManager.class) {
				if (instance == null) {
					instance = new RedisManager();
				}
			}
		}
		return instance;
	}

	public void returnToPool(Jedis j) {
		if (j != null) {
			logger.debug("Jedis Poll returnToPool:" + j.toString());
			j.close();
			j = null;
		}
	}

	public void destroy() {
		pool.destroy();
	}

	public Jedis getResource() {
		logger.debug("Jedis Poll:" + pool.getNumActive() + " " + pool.getNumIdle() + " " + pool.getNumWaiters());
		return pool.getResource();
	}

	public String keyPrefix() {
		return config.getKeyPrefix();
	}
}
