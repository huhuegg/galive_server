package com.galive.common.protocol;

import com.alibaba.fastjson.JSON;

public class CommandOut {

	private Command command;
	
	private String ret_msg = "";
	
	private int ret_code = RetCode.SUCCESS;
	
	public CommandOut(Command command) {
		this.command = command;
		this.ret_code = RetCode.SUCCESS;
	}
	
	public static CommandOut failureOut(Command command, String message) {
		CommandOut out = new CommandOut(command);
		out.setRet_msg(message);
		out.setRet_code(RetCode.FAILURE);
		return out;
	}
	
	public String toJson() {
		return JSON.toJSONString(this);
	}
	
	public String getRet_msg() {
		return ret_msg;
	}

	public void setRet_msg(String ret_msg) {
		this.ret_msg = ret_msg;
	}

	public int getRet_code() {
		return ret_code;
	}

	public void setRet_code(int ret_code) {
		this.ret_code = ret_code;
	}

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}

}
	