package com.galive.logic.network.protocol;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;

public class CommandOut {

	private String command;
	
	private String ret_msg = "";
	
	private String tag = "";
	
	private int ret_code = RetCode.SUCCESS;
	
	public CommandOut(String command) {
		this.command = command;
		this.ret_code = RetCode.SUCCESS;
		this.tag = "";
	}
	
	public static CommandOut failureOut(String command, String message) {
		CommandOut out = new CommandOut(command);
		if (StringUtils.isBlank(message)) {
			out.setRet_msg("内部错误");
		} else {
			out.setRet_msg(message);
		}
		out.setRet_code(RetCode.FAILURE);
		return out;
	}
	
	public String socketResp() {
		return JSON.toJSONString(this);
	}
	
	public String httpResp() {
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

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	

}
	