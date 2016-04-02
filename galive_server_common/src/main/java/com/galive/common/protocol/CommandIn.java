package com.galive.common.protocol;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class CommandIn {

	/**
	 * 请求id
	 */
	private String command;
	
	/**
	 * 用户id
	 */
	private String userSid = "";
	
	/**
	 * token
	 */
	private String token = "";
	
	/**
	 * 请求参数 URL编码 json格式
	 */
	private String params = "";
	
	/**
	 * 从socket接收的消息组装对象 
	 * @param req command&userSid&token&params
	 * @return
	 */
	public static CommandIn fromSocketReq(String req) {
		try {
			String s[] = req.split("&");
			if (s.length != 4) {
				return null;
			}
			String command = s[0];
			if (StringUtils.isBlank(command)) {
				return null;
			}
			String userSid = s[1];
			String token = s[2];
			String params = URLDecoder.decode(s[3], StandardCharsets.UTF_8.name());
			CommandIn in = new CommandIn();
			in.setCommand(command);
			in.setUserSid(userSid);
			in.setToken(token);
			in.setParams(params);
			return in;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从http请求组装对象 
	 * @param req command&
	 * @return
	 */
	public static CommandIn fromHttpReq(HttpServletRequest req) {
		try {
			String command = req.getParameter("command");
			if (StringUtils.isBlank(command)) {
				return null;
			}
			String userSid = req.getParameter("userSid");
			String token = req.getParameter("token");
			String params = URLDecoder.decode(req.getParameter("params"), StandardCharsets.UTF_8.name());
			CommandIn in = new CommandIn();
			in.setCommand(command);
			in.setUserSid(userSid);
			in.setToken(token);
			in.setParams(params);
			return in;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

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

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}
	
	
}
