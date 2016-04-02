package com.galive.common.protocol;

public class Command {

	// 用户注册
	public static final String USR_REGISTER = "USR_REGISTER";
	// 用户登录
	public static final String USR_LOGIN = "USR_LOGIN";
	// 获取用户信息
	public static final String USR_INFO = "USR_INFO";
	// 修改用户信息
	public static final String USR_INFO_MODIFY = "USR_INFO_MODIFY";
	// 客户端上线
	public static final String USR_ONLINE = "USR_ONLINE";
	// 客户端下线
	public static final String USR_OFFLINE = "USR_OFFLINE";
	// 用户列表
	public static final String USR_LIST = "USR_LIST";
	// 创建房间
	public static final String ROOM_CREATE = "ROOM_CREATE";
	// 房间列表
	public static final String ROOM_LIST = "ROOM_LIST";
	// 进入房间
	public static final String ROOM_ENTER = "ROOM_ENTER";
	// 退出房间
	public static final String ROOM_EXIT = "ROOM_EXIT";
	// 客户端发送sdp
	public static final String ROOM_SDP = "ROOM_SDP";
	// 客户端转发
	public static final String TRANSMIT = "CLIENT_TRANSMIT";
	// 客户端上线推送
	public static final String USR_ONLINE_PUSH = "USR_ONLINE_PUSH";
	// 客户端下线推送
	public static final String USR_OFFLINE_PUSH = "USR_OFFLINE_PUSH";
	// 进入房间推送
	public static final String ROOM_ENTER_PUSH = "ROOM_ENTER_PUSH";
	// 退出房间推送
	public static final String ROOM_EXIT_PUSH = "ROOM_EXIT_PUSH";
	// sdp推送
	public static final String ROOM_SDP_PUSH = "ROOM_SDP_PUSH";
	// 房间更新推送
	public static final String ROOM_REFRESH_PUSH = "ROOM_REFRESH_PUSH";
	// 被踢
	public static final String KICK_OFF_PUSH = "KICK_OFF_PUSH";
	// 客户端转发推送
	public static final String TRANSMIT_PUSH = "TRANSMIT_PUSH";

}
