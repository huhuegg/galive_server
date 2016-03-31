package com.galive.logic.dao.db;

import java.util.Properties;

import com.galive.logic.helper.LogicHelper;

public class MongoConfig {

	private MongoConfig() {
		super();
	}
	
	public static MongoConfig loadConfig() {
		MongoConfig config = new MongoConfig();
		try {
			Properties prop = LogicHelper.loadProperties();
			config.host = prop.getProperty("mongo.host");
			config.port = Integer.parseInt(prop.getProperty("mongo.port"));
			config.username = prop.getProperty("mongo.username");
			config.password = prop.getProperty("mongo.password");
			config.dbName = prop.getProperty("mongo.dbName");
			config.poolSize = Integer.parseInt(prop.getProperty("mongo.poolSize"));
			config.threadsAllowedToBlockForConnectionMultiplier = Integer.parseInt(prop.getProperty("mongo.threadsAllowedToBlockForConnectionMultiplier"));
			config.maxWaitTime = Integer.parseInt(prop.getProperty("mongo.maxWaitTime"));
			config.connectTimeout = Integer.parseInt(prop.getProperty("mongo.connectTimeout"));
			config.socketTimeout = Integer.parseInt(prop.getProperty("mongo.socketTimeout"));
			config.socketKeepAlive = Boolean.parseBoolean(prop.getProperty("mongo.socketKeepAlive"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return config;
	}
	
	private String host = "";

	private int port = 0;
	
	private String username = "superman";
	
	private String password = "superman";

	private String dbName = "";

	private int poolSize = 10;

	private int threadsAllowedToBlockForConnectionMultiplier = 5;
	
	private int maxWaitTime = 120000;
	
	private int connectTimeout = 10000;
	
	private int socketTimeout = 0;
	
	private boolean socketKeepAlive = false;

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public int getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	public int getThreadsAllowedToBlockForConnectionMultiplier() {
		return threadsAllowedToBlockForConnectionMultiplier;
	}

	public void setThreadsAllowedToBlockForConnectionMultiplier(
			int threadsAllowedToBlockForConnectionMultiplier) {
		this.threadsAllowedToBlockForConnectionMultiplier = threadsAllowedToBlockForConnectionMultiplier;
	}

	public int getMaxWaitTime() {
		return maxWaitTime;
	}

	public void setMaxWaitTime(int maxWaitTime) {
		this.maxWaitTime = maxWaitTime;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public boolean isSocketKeepAlive() {
		return socketKeepAlive;
	}

	public void setSocketKeepAlive(boolean socketKeepAlive) {
		this.socketKeepAlive = socketKeepAlive;
	}

}
