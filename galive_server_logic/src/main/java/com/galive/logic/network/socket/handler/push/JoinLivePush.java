package com.galive.logic.network.socket.handler.push;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;

public class JoinLivePush extends CommandOut {

	public String account;
	
	public JoinLivePush() {
		super(Command.JOIN_LIVE_PUSH);
	}

	
}
