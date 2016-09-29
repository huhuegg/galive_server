package com.galive.logic.network.socket.handler.push;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;

public class ShareStopPush extends CommandOut {

	public String accountSid;
	
	public ShareStopPush() {
		super(Command.SHARE_STOP_PUSH);
	}

	
}
