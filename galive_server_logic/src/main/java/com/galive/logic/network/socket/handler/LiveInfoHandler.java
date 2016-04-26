package com.galive.logic.network.socket.handler;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Live;
import com.galive.logic.model.User;
import com.galive.logic.network.model.RespLiveInfo;
import com.galive.logic.network.model.RespUser;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.LiveService;
import com.galive.logic.service.LiveServiceImpl;
import com.galive.logic.service.UserService;
import com.galive.logic.service.UserServiceImpl;

@SocketRequestHandler(desc = "获取直播信息", command = Command.LIVE_INFO)
public class LiveInfoHandler extends SocketBaseHandler  {

	private LiveService liveService = new LiveServiceImpl();
	private UserService userService = new UserServiceImpl();
	
	@Override
	public String handle(String userSid, String reqData) throws Exception {
		appendLog("--LiveInfoHandler(获取直播信息)--");
		LiveInfoIn in = JSON.parseObject(reqData, LiveInfoIn.class);
		String liveSid = in.liveSid;
		
		appendLog("直播id(liveSid):" + liveSid);
		
		Live live = liveService.findLive(liveSid);
		
		LiveInfoOut out = new LiveInfoOut();
		RespLiveInfo info = new RespLiveInfo();
		info.convert(live);
		long[] likeNums = liveService.likeNums(liveSid);
		info.likeNum = likeNums[0];
		info.likeAllNum = likeNums[1];
		User user = userService.findUserBySid(live.getOwnerSid());
		RespUser respUser = new RespUser();
		respUser.convert(user);
		info.presenter = respUser;
		out.live = info;
		String resp = out.socketResp();
		return resp;
		
	}
	
	public static class LiveInfoIn {
		public String liveSid = "";
	}
	
	public static class LiveInfoOut extends CommandOut {
		
		public RespLiveInfo live;
		
		public LiveInfoOut() {
			super(Command.LIVE_INFO);
		}
	}
}
