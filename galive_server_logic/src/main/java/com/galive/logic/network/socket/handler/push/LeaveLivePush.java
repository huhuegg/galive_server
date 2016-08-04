package com.galive.logic.network.socket.handler.push;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;

public class LeaveLivePush extends CommandOut {

	public String account;
	
	public LeaveLivePush() {
		super(Command.LEAVE_LIVE_PUSH);
	}

	
}
