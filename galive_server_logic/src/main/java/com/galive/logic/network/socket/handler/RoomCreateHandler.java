package com.galive.logic.network.socket.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.ApplicationMain;
import com.galive.logic.ApplicationMain.ApplicationMode;
import com.galive.logic.helper.APNSHelper;
import com.galive.logic.model.Room;
import com.galive.logic.model.User;
import com.galive.logic.network.model.RespRoom;
import com.galive.logic.network.model.RespUser;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.RoomInviteePush;
import com.galive.logic.service.RoomService;
import com.galive.logic.service.RoomServiceImpl;
import com.galive.logic.service.UserService;
import com.galive.logic.service.UserServiceImpl;

@SocketRequestHandler(desc = "创建房间", command = Command.ROOM_CREATE)
public class RoomCreateHandler extends SocketBaseHandler  {

	private UserService userService = new UserServiceImpl();
	private RoomService roomService = new RoomServiceImpl();
	
	@Override
	public CommandOut handle(String userSid, String reqData) throws Exception {
		appendLog("--RoomCreateHandler(创建房间)--");
		EnterRoomIn in = JSON.parseObject(reqData, EnterRoomIn.class);
		
		String name = in.name;
		String questionSid = in.questionSid;
		List<String> req_invitees = in.invitees;
		int maxUser = in.maxUser;
		
		appendLog("房间名称(name):" + name);
		appendLog("问题id(questionSid):" + questionSid);
		appendLog("房间名称(name):" + name);
		if (!CollectionUtils.isEmpty(req_invitees)) {
			for (String sid : req_invitees) {
				appendLog("邀请用户(invitee):" + sid);
			}
		}
		appendLog("最大人数(maxUser):" + maxUser);
		
		Room room = roomService.create(name, userSid, questionSid, req_invitees, maxUser);
		
		Set<String> invitees = room.getInvitees();
		if (!CollectionUtils.isEmpty(invitees)) {
			User invitor = userService.findUserBySid(userSid);
			
			RoomInviteePush inviteePush = new RoomInviteePush();
			RespRoom inviteeRoom = RespRoom.convert(room);
		
			RespUser respUser = new RespUser();
			respUser.convert(invitor);
			inviteeRoom.invitor = respUser;
			inviteePush.room = inviteeRoom;
			String pushMessage = inviteePush.socketResp();
			appendLog("ROOM_INVITEE_PUSH:" + pushMessage);
			
			List<String> deviceTokens = new ArrayList<>();
			for (String invitee : invitees) {
				String userDesc = userService.findUserBySid(invitee).desc();
				if (userService.isOnline(invitee)) {
					pushMessage(invitee, pushMessage);
					appendLog(String.format("用户%s当前在线, 发送ROOM_INVITEE_PUSH", userDesc));
				} else {
					appendLog(String.format("用户%s当前离线, 无法发送ROOM_INVITEE_PUSH", userDesc));
				}
				String token = userService.findDeviceToken(invitee);
				if (!StringUtils.isBlank(token)) {
					appendLog(String.format("用户%sDeviceToken(%s) 发送苹果推送。", userDesc, token));
					deviceTokens.add(token);
				} else {
					appendLog(String.format("用户%sDeviceToken(%s) 无效。", userDesc, token));
				}
			}
			// 苹果推送
			if (!CollectionUtils.isEmpty(deviceTokens)) {
				APNSHelper apns = new APNSHelper(ApplicationMain.get().getMode() == ApplicationMode.Distribution);
				String content = String.format("%s邀请你加入Ta的房间。", invitor.getNickname());
				apns.push(deviceTokens, content);
				appendLog(String.format("APNS推送信息:" + content));
			}
		}
		
		RoomCreateOut out = new RoomCreateOut();
		out.room = RespRoom.convert(room);
		return out;
	}
	
	public static class EnterRoomIn {
		public String questionSid = "";
		public String name = "";
		public int maxUser = 0;
		public List<String> invitees = new ArrayList<>();
	}
	
	public static class RoomCreateOut extends CommandOut {
		
		public RespRoom room;
		
		public RoomCreateOut() {
			super(Command.ROOM_CREATE);
		}
	}
}
