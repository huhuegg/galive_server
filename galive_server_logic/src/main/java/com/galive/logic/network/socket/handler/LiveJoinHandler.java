package com.galive.logic.network.socket.handler;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Live;
import com.galive.logic.model.User;
import com.galive.logic.network.model.RespLive;
import com.galive.logic.network.model.RespUser;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.LiveJoinPush;
import com.galive.logic.service.LiveService;
import com.galive.logic.service.LiveServiceImpl;
import com.galive.logic.service.UserService;
import com.galive.logic.service.UserServiceImpl;

@SocketRequestHandler(desc = "进入观看直播", command = Command.LIVE_JOIN)
public class LiveJoinHandler extends SocketBaseHandler  {

	private LiveService liveService = new LiveServiceImpl();
	private UserService userService = new UserServiceImpl();
	
	@Override
	public String handle(String userSid, String reqData) throws Exception {
		appendLog("--LiveJoinHandler(进入观看直播)--");
		LiveJoinIn in = JSON.parseObject(reqData, LiveJoinIn.class);
		String liveSid = in.liveSid;
		
		appendLog("直播id(liveSid):" + liveSid);
		
		Live live = liveService.joinLive(liveSid, userSid);
		// 推送
		List<String> audienceSids = liveService.listAllAudiences(live.getSid());
		LiveJoinPush push = new LiveJoinPush();
		RespUser respUser = new RespUser();
		User user = userService.findUserBySid(userSid);
		respUser.convert(user);
		push.user = respUser;
		String pushMessage = push.socketResp();
		
		appendLog("LIVE_JOIN_PUSH:" + pushMessage);
		appendLog("推送用户数量:" + audienceSids.size());
		
		for (String sid : audienceSids) {
			if (!sid.equals(userSid)) {
				if (userService.isOnline(sid)) {
					pushMessage(sid, pushMessage);
				}
			}
		}
		
		
		LiveJoinOut out = new LiveJoinOut();
		RespLive respLive = new RespLive();
		respLive.convert(live);
		out.live = respLive;
		RespUser presenter = new RespUser();
		presenter.convert(userService.findUserBySid(live.getOwnerSid()));
		out.presenter = presenter;
		String resp = out.socketResp();
		return resp;
		
	}
	
	public static class LiveJoinIn {
		public String liveSid = "";
	}
	
	public static class LiveJoinOut extends CommandOut {
		
		public RespLive live;
		public RespUser presenter;
		
		public LiveJoinOut() {
			super(Command.LIVE_JOIN);
		}
	}
	
}
