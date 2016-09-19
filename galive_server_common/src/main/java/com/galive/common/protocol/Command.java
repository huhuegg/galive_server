package com.galive.common.protocol;

public class Command {

	// 用户登录
	public static final String USR_LOGIN = "USR_LOGIN";
	// 用处登出
	public static final String USR_LOGOUT = "USR_LOGOUT";
	// 获取Token
	public static final String REQ_TOKEN = "REQ_TOKEN";
	
	public static final String MEETING_CREATE = "MEETING_CREATE";
	
	public static final String MEETING_DESTROY = "MEETING_DESTROY";
	
	public static final String MEETING_JOIN = "MEETING_JOIN";
	
	public static final String MEETING_LEAVE = "MEETING_LEAVE";
	
	public static final String TRANSMIT = "TRANSMIT";
	
	
	
	public static final String WECHAT_USR_INFO = "WECHAT_USR_INFO";

	public static final String CREATE_LIVE = "CREATE_LIVE";
	public static final String JOIN_LIVE = "JOIN_LIVE";
	public static final String LEAVE_LIVE = "LEAVE_LIVE";
	public static final String DESTROY_LIVE = "DESTROY_LIVE";
	
	public static final String MODIFY_ACCOUNT_INFO = "MODIFY_ACCOUNT_INFO";
	
	public static final String ROOM_LIST = "ROOM_LIST";

	public static final String JOIN_LIVE_PUSH = "JOIN_LIVE_PUSH";
	public static final String LEAVE_LIVE_PUSH = "LEAVE_LIVE_PUSH";
	public static final String DESTROY_LIVE_PUSH = "DESTROY_LIVE_PUSH";

	// 用户被踢下线
	public static final String KICK_OFF_PUSH = "KICK_OFF_PUSH";
	// 用户上线
	public static final String ONLINE = "USR_ONLINE";
	// 用户下线
	public static final String OFFLINE = "USR_OFFLINE";
}
