package com.galive.common.protocol;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CommandIn {

	private static Logger logger = LoggerFactory.getLogger(CommandIn.class);
	/**
	 * 请求id
	 */
	private int command;
	
	/**
	 * 用户账号
	 */
	private String account = "";
	
	/**
	 * token
	 */
	private String token = "";
	
	/**
	 * 请求参数 URL编码 json格式
	 */
	private String params = "";
	
	/**
	 * channelid
	 */
	private String channel = "";
	
	/**
	 * 客户端自定义参数
	 */
	private String tag = "";
	
	/**
	 * 从socket接收的消息组装对象 
	 * @param req command&userSid&token&params
	 * @return
	 */
	public static CommandIn fromSocketReq(String req, String paramsDelimiter) {
		try {
			String s[] = StringUtils.split(req, paramsDelimiter, 6);
			if (s.length < 6) {
				return null;
			}
			String commandStr = s[0];
			if (StringUtils.isBlank(commandStr)) {
				return null;
			}
			int command = Integer.parseInt(commandStr);
			String account = s[1];
			String channel = s[2];
			String token = s[3];
			String tag = s[4];
			String params = URLDecoder.decode(s[5], StandardCharsets.UTF_8.name());
			
			CommandIn in = new CommandIn();
			in.setCommand(command);
			in.setAccount(account);
			in.setChannel(channel);
			in.setTag(tag);
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
			String commandStr = req.getParameter("command");
			if (StringUtils.isBlank(commandStr)) {
				return null;
			}
			int command = Integer.parseInt(commandStr);
			String account = req.getHeader("account");
			String token = req.getHeader("token");
			String params = req.getParameter("params");
			String channel = req.getParameter("channel");
			String tag = req.getParameter("tag");
			logger.debug(String.format("command:%s,account:%s,channel:%s,token:%s,params:%s,tag:%s", command,account,channel,token,params,tag));
			CommandIn in = new CommandIn();
			in.setCommand(command);
			in.setAccount(account);
			in.setChannel(channel);
			in.setToken(token);
			in.setTag(tag);
			if (params != null) {
				in.setParams(URLDecoder.decode(params, StandardCharsets.UTF_8.name()));
			}
			return in;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getCommand() {
		return command;
	}

	public void setCommand(int command) {
		this.command = command;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

}
