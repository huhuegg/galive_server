package com.galive.logic.dao.cache;

import java.util.Properties;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.galive.logic.helper.LogicHelper;


public class RedisConfig {

	private RedisConfig() {
		super();
	}
	
	public static RedisConfig loadConfig() {
		RedisConfig config = new RedisConfig();
		try {
			Properties prop = LogicHelper.loadProperties();
			int port = NumberUtils.toInt(prop.getProperty("redis.port"), 6379);
			int connectTimeout = NumberUtils.toInt(prop.getProperty("redis.connectTimeout"), 10000);
			int maxIdle = NumberUtils.toInt(prop.getProperty("redis.maxIdle"), 1000);
			int maxTotal = NumberUtils.toInt(prop.getProperty("redis.maxTotal"), 5000);
			int maxWaitMillis = NumberUtils.toInt(prop.getProperty("redis.maxWaitMillis"), 60000);
			boolean testOnBorrow = BooleanUtils.toBoolean(prop.getProperty("redis.testOnBorrow"));
			boolean testOnReturn = BooleanUtils.toBoolean(prop.getProperty("redis.testOnReturn"));
			boolean testWhileIdle = BooleanUtils.toBoolean(prop.getProperty("redis.testWhileIdle"));
			
			config.host = prop.getProperty("redis.host");
			config.port = port;
			config.auth = prop.getProperty("redis.auth");
			config.connectTimeout = connectTimeout;
			config.maxIdle = maxIdle;
			config.maxTotal = maxTotal;
			config.maxWaitMillis = maxWaitMillis;
			config.testOnBorrow = testOnBorrow;
			config.testOnReturn = testOnReturn;
			config.testWhileIdle = testWhileIdle;
			config.keyPrefix = prop.getProperty("redis.keyPrefix");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return config;
	}

	private String host = "";

	private int port = 0;
	
	private String auth = "superman";
	
	private int connectTimeout = 60;
	
	private int maxIdle = 0;
	
	private int maxWaitMillis = 30;
	
	private boolean testWhileIdle = true;
	
	private boolean testOnBorrow = true;
	
	private boolean testOnReturn = true;
	
	private int maxTotal = 0;
	
	private String keyPrefix = "app:";

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public void setMaxWaitMillis(int maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	public boolean isTestWhileIdle() {
		return testWhileIdle;
	}

	public void setTestWhileIdle(boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}

	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public boolean isTestOnReturn() {
		return testOnReturn;
	}

	public void setTestOnReturn(boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}

	public String getKeyPrefix() {
		return keyPrefix;
	}

	public void setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	
}
