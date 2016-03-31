package com.galive.logic.config;

public class SocketConfig {

	private String liveReq = "";
	
	private String liveResp = "";
	
	private String host = "";
	
	private String delimiter = "";
	
	private int port = 52194;

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

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
	
}
