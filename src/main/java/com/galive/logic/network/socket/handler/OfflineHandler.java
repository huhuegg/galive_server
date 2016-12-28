package com.galive.logic.network.socket.handler;

import com.galive.logic.network.protocol.Command;
import com.galive.logic.network.protocol.CommandOut;
import com.galive.logic.network.socket.SocketRequestHandler;

@SocketRequestHandler(desc = "用户下线", command = Command.OFFLINE)
public class OfflineHandler extends SocketBaseHandler {


	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--OnlineHandler(用户下线)--");

		return null;
	}

}
