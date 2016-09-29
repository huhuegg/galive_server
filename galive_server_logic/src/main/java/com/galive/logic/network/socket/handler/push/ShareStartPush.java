package com.galive.logic.network.socket.handler.push;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;

public class ShareStartPush extends CommandOut {

	public String accountSid;
	
	public ShareStartPush() {
		super(Command.SHARE_START_PUSH);
	}

	
}
