package com.galive.common.protocol;

public class Command {

	
	// 用户登录
	public static final String USR_LOGIN = "USR_LOGIN";
	// 用户登出
	public static final String USR_LOGOUT = "USR_LOGOUT";
	// 用户接口
	public static final String USR_INFO = "USR_INFO";
	// 获取Token
	public static final String REQ_TOKEN = "REQ_TOKEN";
	
	public static final String MEETING_START = "MEETING_START";
	
	public static final String MEETING_STOP = "MEETING_STOP";
	
	public static final String MEETING_JOIN = "MEETING_JOIN";
	
	public static final String MEETING_LEAVE = "MEETING_LEAVE";
	
	public static final String MEETING_MEMBER_KICK = "MEETING_MEMBER_KICK";
	
	public static final String MEETING_LIST = "MEETING_LIST";
	
	public static final String MEETING_INFO = "MEETING_INFO";
	
	public static final String TRANSMIT = "TRANSMIT";
	
	public static final String SHARE_START = "SHARE_START";
	
	public static final String SHARE_STOP = "SHARE_STOP";
	
	public static final String STOP_MEETING_PUSH = "STOP_MEETING_PUSH";
	
	public static final String JOIN_MEETING_PUSH = "JOIN_MEETING_PUSH";
	
	public static final String LEAVE_MEETING_PUSH = "LEAVE_MEETING_PUSH";
	
	public static final String TRANSMIT_PUSH = "TRANSMIT_PUSH";
	
	public static final String SHARE_START_PUSH = "SHARE_START_PUSH";
	
	public static final String SHARE_STOP_PUSH = "SHARE_STOP_PUSH";
	
	public static final String MEETING_MEMBER_KICK_PUSH = "MEETING_MEMBER_KICK_PUSH";
	

	// 用户被踢下线
	public static final String KICK_OFF_PUSH = "KICK_OFF_PUSH";
	// 用户上线
	public static final String ONLINE = "USR_ONLINE";
	// 用户下线
	public static final String OFFLINE = "USR_OFFLINE";
}
