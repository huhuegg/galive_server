package com.galive.logic.network.protocol;

public class Command {
	
	// 用户登录
	public static final String USR_LOGIN = "USR_LOGIN";
	// 用户接口
	public static final String USR_INFO = "USR_INFO";

	public static final String ROOM_INFO = "ROOM_INFO";
	
	public static final String ROOM_CREATE = "ROOM_CREATE";
	
	public static final String ROOM_ENTER = "ROOM_ENTER";
	
	public static final String ROOM_EXIT = "ROOM_EXIT";
	
	public static final String ROOM_DESTROY = "ROOM_DESTROY";
	
	public static final String SCREEN_SHARE_START = "SHARE_START";
	
	public static final String SCREEN_SHARE_STOP = "SHARE_STOP";
	
	public static final String ROOM_ENTER_PUSH = "ROOM_ENTER_PUSH";
	
	public static final String ROOM_EXIT_PUSH = "ROOM_EXIT_PUSH";
	
	public static final String ROOM_DESTROY_PUSH = "ROOM_DESTROY_PUSH";
	
	public static final String SCREEN_SHARE_START_PUSH = "SCREEN_SHARE_START_PUSH";
	
	public static final String SCREEN_SHARE_STOP_PUSH = "SCREEN_SHARE_STOP_PUSH";
	
	public static final String TRANSMIT = "TRANSMIT";
	
	public static final String TRANSMIT_PUSH = "TRANSMIT_PUSH";

	// 用户被踢下线
	public static final String KICK_OFF_PUSH = "KICK_OFF_PUSH";
	// 用户上线
	public static final String ONLINE = "USR_ONLINE";
	// 用户下线
	public static final String OFFLINE = "USR_OFFLINE";


	public static final String BIND_PC_CLIENT = "BIND_PC_CLIENT";

}
