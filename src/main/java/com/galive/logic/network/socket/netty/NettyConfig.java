package com.galive.logic.network.socket.netty;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.math.NumberUtils;

import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.helper.LogicHelper;


public class NettyConfig {

	private int port = 52194;

	private int bufferSize;

	private int bothIdleTime;

	private String liveReq = "";

	private String liveResp = "";

	public static NettyConfig loadConfig()  {
		NettyConfig config = new NettyConfig();
		InputStream in = null;
		try {
			in = LogicHelper.loadProperties();
			Properties prop = new Properties(); 
			LogicHelper.loadProperties();
			prop.load(in);
			config.port = ApplicationConfig.getInstance().getSocketConfig().getPort();
			
			int bufferSize = NumberUtils.toInt(prop.getProperty("netty.bufferSize"), 8192);
			int idleTime = NumberUtils.toInt(prop.getProperty("netty.bothIdleTime"), 60);
			
			config.bufferSize = bufferSize;
			config.bothIdleTime = idleTime;
			config.liveReq = ApplicationConfig.getInstance().getSocketConfig().getLiveReq();
			config.liveResp = ApplicationConfig.getInstance().getSocketConfig().getLiveResp();
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
}
