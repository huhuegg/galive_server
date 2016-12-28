package com.galive.logic.network.socket.handler.push;

import com.galive.logic.protocol.Command;
import com.galive.logic.protocol.CommandOut;

public class KickOffPush extends CommandOut {
	
	public KickOffPush() {
		super(Command.KICK_OFF_PUSH);
	}

	
}
