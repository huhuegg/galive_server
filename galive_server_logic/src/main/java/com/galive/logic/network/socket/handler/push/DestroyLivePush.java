package com.galive.logic.network.socket.handler.push;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;

public class DestroyLivePush extends CommandOut {
	
	public String account;
	
	public DestroyLivePush() {
		super(Command.DESTROY_LIVE_PUSH);
	}

	
}
