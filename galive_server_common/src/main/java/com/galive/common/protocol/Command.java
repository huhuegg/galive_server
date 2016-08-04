package com.galive.common.protocol;

public class Command {

	// 获取Token
	public static final int REQ_TOKEN = 1000;

	public static final int CREATE_LIVE = 1050;
	public static final int JOIN_LIVE = 1051;
	public static final int LEAVE_LIVE = 1052;
	public static final int DESTROY_LIVE = 1053;

	public static final int JOIN_LIVE_PUSH = 3051;
	public static final int LEAVE_LIVE_PUSH = 3052;
	public static final int DESTROY_LIVE_PUSH = 3053;

	// 用户被踢下线
	public static final int KICK_OFF_PUSH = 1997;
	// 用户上线
	public static final int ONLINE = 1998;
	// 用户下线
	public static final int OFFLINE = 1999;
}
