package com.galive.logic.config;

import java.util.ArrayList;
import java.util.List;

public class RTCConfig {
	
	private String turnUrl;
	
	private List<IceServer> iceServers = new ArrayList<>();
	
	public static class IceServer {
		public String url;
	}

	public String getTurnUrl() {
		return turnUrl;
	}

	public void setTurnUrl(String turnUrl) {
		this.turnUrl = turnUrl;
	}

	public List<IceServer> getIceServers() {
		return iceServers;
	}

	public void setIceServers(List<IceServer> iceServers) {
		this.iceServers = iceServers;
	}

}
