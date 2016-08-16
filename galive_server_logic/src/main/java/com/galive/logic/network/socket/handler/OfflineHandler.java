package com.galive.logic.network.socket.handler;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.network.socket.SocketRequestHandler;

@SocketRequestHandler(desc = "用户下线", command = Command.OFFLINE)
public class OfflineHandler extends SocketBaseHandler {

	

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--OnlineHandler(用户下线)--");

		// 清除直播信息
//		liveService.clearLiveForAccount(account);
//		CommandOut out = new CommandOut(Command.OFFLINE);
//		return out;
		return null;
	}

}
