package com.galive.common.protocol;

public class Command {

	// 用户注册
	public static final String USR_REGISTER = "CMDUserRegister";
	// 用户登录
	public static final String USR_LOGIN = "CMDUserLogin";
	// 获取用户信息
	public static final String USR_INFO = "CMDUserInfo";
	// 修改用户信息
	public static final String USR_INFO_MODIFY = "CMDUserInfoModify";
	// 客户端上线
	public static final String USER_ONLINE = "CMDUserOnline";
	// 客户端下线
	public static final String USER_OFFLINE = "CMDUserOffline";
	// 获取用户信息
	public static final String USER_INFO = "CMDUserInfo";
	// 用户列表
	public static final String USER_LIST = "CMDUserList";
	// 创建房间
	public static final String ROOM_CREATE = "CMDRoomCreate";
	// 房间列表
	public static final String ROOM_LIST = "CMDRoomList";
	// 进入房间
	public static final String ROOM_ENTER = "CMDRoomEnter";
	// 退出房间
	public static final String ROOM_EXIT = "CMDRoomExit";
	// 客户端发送sdp
	public static final String ROOM_SDP = "CMDRoomSDP";
	// 客户端转发
	public static final String CLIENT_TRANSMIT = "CMDClientTransmit";

	// 客户端上线推送
	public static final String USER_ONLINE_PUSH = "CMDUserOnlinePush";

	// 客户端下线推送
	public static final String USER_OFFLINE_PUSH = "CMDUserOfflinePush";

	// 进入房间推送
	public static final String ROOM_ENTER_PUSH = "CMDRoomEnterPush";
	// 退出房间推送
	public static final String ROOM_EXIT_PUSH = "CMDRoomExitPush";
	// sdp推送
	public static final String ROOM_SDP_PUSH = "CMDRoomSDPPush";
	// 客户端转发推送
	public static final String CLIENT_TRANSMIT_PUSH = "CMDClientTransmitPush";
	// 房间更新推送
	public static final String ROOM_REFRESH_PUSH = "CMDRoomRefreshPush";
	// 被踢
	public static final String KICK_OFF_PUSH = "CMDKickOffPush";

}
