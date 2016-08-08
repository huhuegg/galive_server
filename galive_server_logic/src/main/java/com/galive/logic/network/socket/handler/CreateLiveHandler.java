package com.galive.logic.network.socket.handler;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Live;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.LiveService;
import com.galive.logic.service.LiveServiceImpl;

@SocketRequestHandler(desc = "创建直播", command = Command.CREATE_LIVE)
public class CreateLiveHandler extends SocketBaseHandler {

	private LiveService liveService = new LiveServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--CreateLiveHandler(创建直播)--");
		Live live = liveService.createLive(account);
		CreateLiveOut out = new CreateLiveOut();
		out.live = live;
		return out;
	}

	public static class CreateLiveOut extends CommandOut {

		public CreateLiveOut() {
			super(Command.CREATE_LIVE);
		}

		public Live live;
	}

}
