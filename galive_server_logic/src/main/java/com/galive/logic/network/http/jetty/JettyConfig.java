package com.galive.logic.network.http.jetty;

import java.util.Properties;

import com.galive.logic.helper.LogicHelper;

public class JettyConfig {

	private int port;
	
	private String action;
	
	public static JettyConfig loadConfig() {
		JettyConfig config = new JettyConfig();
		try {
			Properties prop = LogicHelper.loadProperties();
			config.port = Integer.parseInt(prop.getProperty("http.port", "8080"));
			config.action = prop.getProperty("http.action.logic", "/logic");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return config;
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
}
