package com.galive.logic.network.socket.netty;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.math.NumberUtils;

import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.helper.LogicHelper;


public class NettyConfig {

	private int port = 52194;

	private int bufferSize = 2048;

	private int bothIdleTime = 10;

	private int heartBeat = 30;

	private int writeTimeout = 30;

	private String liveReq = "";

	private String liveResp = "";

	public static NettyConfig loadConfig() throws IOException {
		NettyConfig config = new NettyConfig();
		Properties prop = LogicHelper.loadProperties();
		config.port = ApplicationConfig.getInstance().getSocketConfig().getPort();
		
		int bufferSize = NumberUtils.toInt(prop.getProperty("netty.bufferSize"), 8192);
		int idleTime = NumberUtils.toInt(prop.getProperty("netty.bothIdleTime"), 60);
		int heartBeaiInterval = NumberUtils.toInt(prop.getProperty("netty.heartBeat.interval"), 30);
		int writeTimeout = NumberUtils.toInt(prop.getProperty("netty.writeTimeout"), 30);
		
		config.bufferSize = bufferSize;
		config.bothIdleTime = idleTime;
		config.heartBeat = heartBeaiInterval;
		config.writeTimeout = writeTimeout;
		config.liveReq = ApplicationConfig.getInstance().getSocketConfig().getLiveReq();
		config.liveResp = ApplicationConfig.getInstance().getSocketConfig().getLiveResp();
		return config;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public int getBothIdleTime() {
		return bothIdleTime;
	}

	public void setBothIdleTime(int bothIdleTime) {
		this.bothIdleTime = bothIdleTime;
	}

	public String getLiveReq() {
		return liveReq;
	}

	public void setLiveReq(String liveReq) {
		this.liveReq = liveReq;
	}

	public String getLiveResp() {
		return liveResp;
	}

	public void setLiveResp(String liveResp) {
		this.liveResp = liveResp;
	}

	public int getHeartBeat() {
		return heartBeat;
	}

	public void setHeartBeat(int heartBeat) {
		this.heartBeat = heartBeat;
	}

	public int getWriteTimeout() {
		return writeTimeout;
	}

	public void setWriteTimeout(int writeTimeout) {
		this.writeTimeout = writeTimeout;
	}
}
