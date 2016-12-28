package com.galive.logic.network.socket.handler.push;

import com.galive.logic.network.protocol.Command;
import com.galive.logic.network.protocol.CommandOut;

public class ScreenShareStopPush extends CommandOut {

	public String accountSid;
	
	public ScreenShareStopPush() {
		super(Command.SCREEN_SHARE_STOP_PUSH);
	}

	
}
