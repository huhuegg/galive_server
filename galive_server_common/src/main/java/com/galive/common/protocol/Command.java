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
	// 创建问题
	public static final String QUESTION_CREATE = "QUESTION_CREATE";
	// 创建详情
	public static final String QUESTION_INFO = "QUESTION_INFO";
	// 问题列表
	public static final String QUESTION_LIST = "QUESTION_LIST";
	// 创建解答
	public static final String ANSWER_CREATE = "ANSWER_CREATE";
	
	// 开始直播
	public static final String LIVE_START = "LIVE_START";
	// 发送直播消息
	public static final String LIVE_MESSAGE_SEND = "LIVE_MESSAGE_SEND";
	// 结束直播
	public static final String LIVE_STOP = "LIVE_STOP";
	// 直播列表
	public static final String LIVE_LIST = "LIVE_LIST";
	// 观看直播
	public static final String LIVE_JOIN = "LIVE_JOIN";
	// 退出观看直播
	public static final String LIVE_LEAVE = "LIVE_LEAVE";
	// 直播观众列表
	public static final String LIVE_AUDIENCE_LIST = "LIVE_AUDIENCE_LIST";
	// 直播点赞
	public static final String LIVE_LIKE = "LIVE_LIKE";
	// 获取直播信息
	public static final String LIVE_INFO = "LIVE_INFO";

	// 客户端上线推送
	public static final String USR_ONLINE_PUSH = "USR_ONLINE_PUSH";
	// 客户端下线推送
	public static final String USR_OFFLINE_PUSH = "USR_OFFLINE_PUSH";
	// 进入房间推送
	public static final String ROOM_ENTER_PUSH = "ROOM_ENTER_PUSH";
	// 被邀请进入房间推送
	public static final String ROOM_INVITEE_PUSH = "ROOM_INVITEE_PUSH";
	// 退出房间推送
	public static final String ROOM_EXIT_PUSH = "ROOM_EXIT_PUSH";
	// sdp推送
	public static final String ROOM_SDP_PUSH = "ROOM_SDP_PUSH";
	// 被踢
	public static final String KICK_OFF_PUSH = "KICK_OFF_PUSH";
	// 客户端转发推送
	public static final String TRANSMIT_PUSH = "TRANSMIT_PUSH";

	// 进入观看直播推送
	public static final String LIVE_JOIN_PUSH = "LIVE_JOIN_PUSH";
	// 退出观看直播推送
	public static final String LIVE_LEAVE_PUSH = "LIVE_LEAVE_PUSH";
	// 停止直播推送
	public static final String LIVE_STOP_PUSH = "LIVE_STOP_PUSH";
	// 直播消息推送
	public static final String LIVE_MESSAGE_PUSH = "LIVE_MESSAGE_PUSH";
	// 直播点赞推送
	public static final String LIVE_LIKE_PUSH = "LIVE_LIKE_PUSH";

}
