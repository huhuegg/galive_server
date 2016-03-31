package com.galive.logic.dao.db;

import java.util.Properties;
import com.galive.logic.helper.LogicHelper;


public class RedisConfig {

	private RedisConfig() {
		super();
	}
	
	public static RedisConfig loadConfig() {
		RedisConfig config = new RedisConfig();
		try {
			Properties prop = LogicHelper.loadProperties();
			config.host = prop.getProperty("redis.host");
			config.port = Integer.parseInt(prop.getProperty("redis.port"));
			config.auth = prop.getProperty("redis.auth");
			config.connectTimeout = Integer.parseInt(prop.getProperty("redis.connectTimeout"));
			config.maxIdle = Integer.parseInt(prop.getProperty("redis.maxIdle"));
			config.maxTotal = Integer.parseInt(prop.getProperty("redis.maxTotal"));
			config.maxWaitMillis = Integer.parseInt(prop.getProperty("redis.maxWaitMillis"));
			config.testOnBorrow = Boolean.parseBoolean(prop.getProperty("redis.testOnBorrow"));
			config.testOnReturn = Boolean.parseBoolean(prop.getProperty("redis.testOnReturn"));
			config.testWhileIdle = Boolean.parseBoolean(prop.getProperty("redis.testWhileIdle"));
			config.keyPrefix = prop.getProperty("redis.keyPrefix");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return config;
	}

	private String host = "";

	private int port = 0;
	
	private String auth = "superman";
	
	private int connectTimeout = 10000;
	
	private int maxIdle = 0;
	
	private int maxWaitMillis = 0;
	
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
