package com.galive.common.protocol;

public enum Command {

	// 用户注册
	USR_REGISTER("UserRegister"),
	// 用户登录
	USR_LOGIN("UserLogin"),
	// 获取用户信息
	USR_INFO("UserInfo"),
	// 修改用户信息
	USR_INFO_MODIFY("UserInfoModify"),
	// 客户端上线
	USER_ONLINE("UserOnline"),
	// 客户端下线
	USER_OFFLINE("UserOffline"),
	// 创建房间
	ROOM_CREATE("RoomCreate"),
	// 房间列表
	ROOM_LIST("RoomList"),
	// 进入房间
	ROOM_ENTER("RoomEnter"),
	// 退出房间
	ROOM_EXIT("RoomExit"),
	// 客户端发送sdp
	ROOM_SDP("RoomSDP"),
	// 客户端转发
	CLIENT_TRANSMIT("ClientTransmit"),
	
	//---------------推送---------------//
	// 客户端上线推送
	USER_ONLINE_PUSH("UserOnlinePush"),
	// 客户端下线推送
	USER_OFFLINE_PUSH("UserOfflinePush"),
	// 进入房间推送
	ROOM_ENTER_PUSH("RoomEnterPush"),
	// 退出房间推送
	ROOM_EXIT_PUSH("RoomExitPush"),
	// sdp推送
	ROOM_SDP_PUSH("RoomSDPPush"),
	// 房间更新推送
	ROOM_REFRESH_PUSH("RoomRefreshPush"),
	// 客户端转发推送
	CLIENT_TRANSMIT_PUSH("ClientTransmitPush"),
	// 被踢
	KICK_OFF_PUSH("KickOffPush");
	
	
	
	private String code;
	
	private Command(String code) {
		this.code = code;
	}

	public String getCode() {
		return "CMD" + code;
	}

}
