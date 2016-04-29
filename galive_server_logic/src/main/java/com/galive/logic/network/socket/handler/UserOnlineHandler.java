package com.galive.logic.network.socket.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Room;
import com.galive.logic.model.Room.RoomType;
import com.galive.logic.model.User;
import com.galive.logic.network.model.RespQuestion;
import com.galive.logic.network.model.RespRoom;
import com.galive.logic.network.model.RespUser;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.UserOnlinePush;
import com.galive.logic.service.QuestionService;
import com.galive.logic.service.QuestionServiceImpl;
import com.galive.logic.service.RoomService;
import com.galive.logic.service.RoomServiceImpl;
import com.galive.logic.service.UserService;
import com.galive.logic.service.UserServiceImpl;

@SocketRequestHandler(desc = "客户端上线", command = Command.USR_ONLINE)
public class UserOnlineHandler extends SocketBaseHandler {

	private UserService userService = new UserServiceImpl();
	private RoomService roomService = new RoomServiceImpl();
	private QuestionService questionService = new QuestionServiceImpl();
	
	@Override
	public CommandOut handle(String userSid, String reqData) throws Exception {
		appendLog("--UserOnlineHandler(用户上线)--");
		
		UserOnlineOut out = new UserOnlineOut();
		
		Room room = roomService.findRoomByUser(userSid);
		if (room != null) {
			User u = userService.findUserBySid(userSid);
			RespRoom respRoom = RespRoom.convert(room);
			RespUser ru = new RespUser();
			ru.convert(u);
			UserOnlinePush push = new UserOnlinePush();
			push.user = ru;
			String pushMessage = push.socketResp();
			appendLog(String.format("用户%s在房间" + room.desc() + "内 ,USR_ONLINE_PUSH:%s", u.desc(), pushMessage));
		
			Set<String> roomUsers = room.getUsers();
			for (String roomUserSid : roomUsers) {
				if (!roomUserSid.equals(userSid)) {
					String userDesc = userService.findUserBySid(roomUserSid).desc();
					if (userService.isOnline(roomUserSid)) {
						pushMessage(roomUserSid, pushMessage);
						appendLog(String.format("用户%s当前在线, 发送USR_ONLINE_PUSH", userDesc));
					} else {
						appendLog(String.format("用户%s当前离线, 无法发送USR_ONLINE_PUSH", userDesc));
					}
					User roomUser = userService.findUserBySid(roomUserSid);
					RespUser roomRespUser = new RespUser();
					roomRespUser.convert(roomUser);
					respRoom.users.add(roomRespUser);
				}
			}				
			out.room = respRoom;
		}
		

		Room inviteeRoom = roomService.findRoomByInvitee(userSid);
		if (inviteeRoom != null) {
			RespRoom respRoom = RespRoom.convert(inviteeRoom);
			RespUser invitor = new RespUser();
			invitor.convert(userService.findUserBySid(inviteeRoom.getOwnerId()));
			respRoom.invitor = invitor;
			if (inviteeRoom.getType() == RoomType.Question) {
				RespQuestion rq = new RespQuestion();
				rq.convert(questionService.findQuestionBySid(inviteeRoom.getQuestionSid()));
				respRoom.question = rq;
			}
			out.inviteeRoom = respRoom;
		}
		List<String> questionTags = questionService.listQuestionTags();
		out.tags = questionTags;
		return out;
	}
	
	public static class UserOnlineOut extends CommandOut {

		public UserOnlineOut() {
			super(Command.USR_ONLINE);
		}

		public RespRoom room;
		public RespRoom inviteeRoom;
		public List<String> tags = new ArrayList<>();
	}
	
}
