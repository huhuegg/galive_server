package com.galive.logic.network.socket.handler;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Live;
import com.galive.logic.network.model.RespLive;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.LiveStopPush;
import com.galive.logic.service.LiveService;
import com.galive.logic.service.LiveServiceImpl;
import com.galive.logic.service.UserService;
import com.galive.logic.service.UserServiceImpl;

@SocketRequestHandler(desc = "结束直播", command = Command.LIVE_STOP)
public class LiveStopHandler extends SocketBaseHandler {

	private UserService userService = new UserServiceImpl();
	private LiveService liveService = new LiveServiceImpl();

	@Override
	public CommandOut handle(String userSid, String reqData) throws Exception {
		appendLog("--LiveStopHandler(结束直播)--");
		LiveStopIn in = JSON.parseObject(reqData, LiveStopIn.class);
		Live live = liveService.stopLive(userSid, in.actionRecordUrl);
		if (live != null) {
			// 推送
			List<String> audienceSids = liveService.listAllAudiences(live.getSid());
			LiveStopPush push = new LiveStopPush();
			RespLive respLive = new RespLive();
			respLive.convert(live);
			push.live = respLive;
			String pushMessage = push.socketResp();
			appendLog("LIVE_STOP_PUSH:" + pushMessage);
			appendLog("推送用户数量:" + audienceSids.size());
			for (String sid : audienceSids) {
				if (!sid.equals(userSid)) {
					if (userService.isOnline(sid)) {
						pushMessage(sid, pushMessage);
					}
				}
			}
		}

		CommandOut out = new CommandOut(Command.LIVE_STOP);
		return out;
	}

	public static class LiveStopIn {
		public String actionRecordUrl;
	}

}
