package com.galive.common.protocol;

public class CommandIn {

	private Command command;
	
	private String userSid = "";
	
	private String token = "";

	public String getUserSid() {
		return userSid;
	}

	public void setUserSid(String userSid) {
		this.userSid = userSid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}
	
	
}
