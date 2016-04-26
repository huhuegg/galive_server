package com.galive.logic.network.socket.handler;

import java.util.List;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Live;
import com.galive.logic.model.User;
import com.galive.logic.network.model.RespUser;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.LiveLikePush;
import com.galive.logic.service.LiveService;
import com.galive.logic.service.LiveServiceImpl;
import com.galive.logic.service.UserService;
import com.galive.logic.service.UserServiceImpl;

@SocketRequestHandler(desc = "直播点赞", command = Command.LIVE_LIKE)
public class LiveLikeHandler extends SocketBaseHandler {

	private LiveService liveService = new LiveServiceImpl();
	private UserService userService = new UserServiceImpl();
	
	@Override
	public String handle(String userSid, String reqData) throws Exception {
		appendLog("--LiveLikeHandler(直播点赞)--");

		Live live = liveService.findLiveByAudience(userSid);
		if (live != null) {
			liveService.doLike(live.getSid(), userSid);
			// 推送
			List<String> audienceSids = liveService.listAllAudiences(live.getSid());
			LiveLikePush push = new LiveLikePush();
			RespUser respUser = new RespUser();
			User user = userService.findUserBySid(userSid);
			respUser.convert(user);
			push.user = respUser;
			String pushMessage = push.socketResp();
			appendLog("LIVE_LIKE_PUSH:" + pushMessage);
			appendLog("推送用户数量:" + audienceSids.size());
			for (String sid : audienceSids) {
				if (!sid.equals(userSid)) {
					if (userService.isOnline(sid)) {
						pushMessage(sid, pushMessage);
					}
				}
			}
		}
		CommandOut out = new CommandOut(Command.LIVE_LIKE);
		String resp = out.socketResp();
		return resp;
	}
}
