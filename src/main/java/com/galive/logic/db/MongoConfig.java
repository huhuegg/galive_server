package com.galive.logic.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.galive.logic.helper.LogicHelper;

public class MongoConfig {

	private MongoConfig() {
		super();
	}
	
	public static MongoConfig loadConfig() {
		MongoConfig config = new MongoConfig();
		InputStream in = null;
		try {
			in = LogicHelper.loadProperties();
			Properties prop = new Properties(); LogicHelper.loadProperties();
			prop.load(in);
			int poolSize = NumberUtils.toInt(prop.getProperty("mongo.poolSize"), 10);
			int maxWaitTime = NumberUtils.toInt(prop.getProperty("mongo.maxWaitTime"), 120000);
			int connectTimeout = NumberUtils.toInt(prop.getProperty("mongo.connectTimeout"), 10000);
			int socketTimeout = NumberUtils.toInt(prop.getProperty("mongo.socketTimeout"), 0);
			int threadsAllowedToBlockForConnectionMultiplier = NumberUtils.toInt(prop.getProperty("mongo.threadsAllowedToBlockForConnectionMultiplier"), 5);
			int port = NumberUtils.toInt(prop.getProperty("mongo.port"), 27017);
			boolean keepAlive = BooleanUtils.toBoolean(prop.getProperty("mongo.socketKeepAlive"));
			
			config.host = prop.getProperty("mongo.host");
			config.port = port;
			config.username = prop.getProperty("mongo.username");
			config.password = prop.getProperty("mongo.password");
			config.dbName = prop.getProperty("mongo.dbName");
			config.poolSize = poolSize;
			config.threadsAllowedToBlockForConnectionMultiplier = threadsAllowedToBlockForConnectionMultiplier;
			config.maxWaitTime = maxWaitTime;
			config.connectTimeout = connectTimeout;
			config.socketTimeout = socketTimeout;
			config.socketKeepAlive = keepAlive;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
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
