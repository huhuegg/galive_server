package com.galive.logic.network.socket.handler;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.LiveService;
import com.galive.logic.service.LiveServiceImpl;

@SocketRequestHandler(desc = "用户上线", command = Command.ONLINE)
public class OnlineHandler extends SocketBaseHandler {

	private LiveService liveService = new LiveServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--OnlineHandler(用户上线)--");

		// 清除直播信息
		liveService.clearLiveForAccount(account);
		CommandOut out = new CommandOut(Command.ONLINE);
		return out;
	}

}
