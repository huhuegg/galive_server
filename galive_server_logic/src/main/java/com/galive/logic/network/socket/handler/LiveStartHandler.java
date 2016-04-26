package com.galive.logic.network.socket.handler;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Live;
import com.galive.logic.network.model.RespLive;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.LiveService;
import com.galive.logic.service.LiveServiceImpl;

@SocketRequestHandler(desc = "开始直播", command = Command.LIVE_START)
public class LiveStartHandler extends SocketBaseHandler {

	private LiveService liveService = new LiveServiceImpl();
	
	@Override
	public String handle(String userSid, String reqData) throws Exception {
		appendLog("--LiveStartHandler(开始直播)--");
		Live live = liveService.startLive(userSid);
		
		LiveStartOut out = new LiveStartOut();
		RespLive respLive = new RespLive();
		respLive.convert(live);
		out.live = respLive;
		String resp = out.socketResp();
		return resp;
		
	}
	
	public static class LiveStartOut extends CommandOut {
		
		public RespLive live;
		
		public LiveStartOut() {
			super(Command.LIVE_START);
		}
	}
}
