package com.galive.logic.config;

import java.io.IOException;
import java.util.Properties;

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
		config.bufferSize = Integer.parseInt(prop.getProperty("netty.bufferSize"));
		config.bothIdleTime = Integer.parseInt(prop.getProperty("netty.bothIdleTime"));
		config.heartBeat = Integer.parseInt(prop.getProperty("netty.heartBeat"));
		config.writeTimeout = Integer.parseInt(prop.getProperty("netty.writeTimeout"));
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
