package com.galive.logic.network.socket.handler;

import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandIn;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Room;
import com.galive.logic.model.Room.RoomType;
import com.galive.logic.model.User;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.RoomExitPush;
import com.galive.logic.network.socket.handler.push.RoomExitPush.RoomExitType;
import com.galive.logic.service.RoomService;
import com.galive.logic.service.RoomServiceImpl;
import com.galive.logic.service.UserService;
import com.galive.logic.service.UserServiceImpl;

@SocketRequestHandler(desc = "退出房间", command = Command.ROOM_EXIT)
public class RoomExitHandler extends SocketBaseHandler  {

	private UserService userService = new UserServiceImpl();
	private RoomService roomService = new RoomServiceImpl();
	
	@Override
	public CommandOut handle(String userSid, String reqData) throws Exception {
		appendLog("--RoomExitHandler(退出房间)--");
		RoomExitIn in = JSON.parseObject(reqData, RoomExitIn.class);
		String roomSid = in.roomSid;
		
		appendLog("房间id(roomSid):" + roomSid);
		
		Room inviteeRoom = roomService.findRoomByInvitee(userSid);
		if (inviteeRoom != null && inviteeRoom.getSid().equals(roomSid)) {
			// 拒绝进入房间
			Room room = roomService.refuseInvite(userSid);
			// 推送邀请人
			User invitor = userService.findUserBySid(room.getOwnerId());
			RoomExitPush push = new RoomExitPush();
			push.userSid = userSid;
			
			if (inviteeRoom.getType() == RoomType.Question) {
				push.type = RoomExitType.RefuseQuestionInvite.ordinal();
			} else {
				push.type = RoomExitType.RefuseInvite.ordinal();
			}
			String pushMessage = push.socketResp();
			appendLog(String.format("拒绝房间(%s)邀请,推送房主%s", inviteeRoom.getSid(), invitor.desc() + pushMessage));
			pushMessage(invitor.getSid(), pushMessage);
		} else {
			Room room = roomService.exit(userSid);
			if (room != null) {
				// 推送
				RoomExitPush push = new RoomExitPush();
				push.type = RoomExitType.ExitRoom.ordinal();
				push.userSid = userSid;
				String pushMessage = push.socketResp();
				appendLog("ROOM_EXIT_PUSH:" + pushMessage);
				Set<String> users = room.getUsers();
				for (String roomerSid : users) {
					if (!roomerSid.equals(userSid)) {
						String userDesc = userService.findUserBySid(roomerSid).desc();
						if (userService.isOnline(roomerSid)) {
							pushMessage(roomerSid, pushMessage);
							appendLog(String.format(String.format("用户%s当前在线, 发送ROOM_EXIT_PUSH", userDesc)));
						} else {
							appendLog(String.format("用户%s当前离线, 无法发送ROOM_EXIT_PUSH", userDesc));
						}
					}
				}
			}
		}
		CommandOut out = new CommandOut(Command.ROOM_EXIT);
		return out;
	}
	
	public static class RoomExitIn extends CommandIn {
		public String roomSid;
	}

}
