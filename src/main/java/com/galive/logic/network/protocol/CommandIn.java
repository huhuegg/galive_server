package com.galive.logic.network.protocol;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CommandIn {

	private static Logger logger = LoggerFactory.getLogger(CommandIn.class);
	
	private static final String MAGIC_KEY = "lllfdfgert3242342sfsdfsad";
	
	/**
	 * 请求id
	 */
	private String command;
	
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
			String s[] = StringUtils.split(req, paramsDelimiter, 5);
			if (s.length < 5) {
				return null;
			}
			String commandStr = s[0];
			if (StringUtils.isBlank(commandStr)) {
				return null;
			}
			String command = commandStr;
			String account = s[1];
			String token = s[2];
			String tag = s[3];
			String params = URLDecoder.decode(s[4], StandardCharsets.UTF_8.name());
			
			CommandIn in = new CommandIn();
			in.setCommand(command);
			in.setAccount(account);
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
			String command = req.getParameter("command");
			if (StringUtils.isBlank(command)) {
				return null;
			}
			String account = req.getParameter("account");
			String token = req.getParameter("token");
			String params = req.getParameter("params");
			String tag = req.getParameter("tag");
			String s = req.getParameter("s");
			logger.debug(String.format("command:%s,account:%s,token:%s,params:%s,tag:%s,s:%s", command,account,token,params,tag,s));
			String planText = String.format("<%s>", account + params + MAGIC_KEY);
			String md5 = DigestUtils.md5Hex(planText);
			if (!md5.equals(s)) {
				return null;
			}
			CommandIn in = new CommandIn();
			in.setCommand(command);
			in.setAccount(account);
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

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

}
