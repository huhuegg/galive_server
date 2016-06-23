package com.galive.logic.network.socket.handler.push;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.network.model.RespUser;

public class LiveMessagePush extends CommandOut {

	public RespUser sender;
	public String content;
	
	public LiveMessagePush() {
		super(Command.LIVE_MESSAGE_PUSH);
	}
}
