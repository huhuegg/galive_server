package com.galive.logic.network.socket.handler.push;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;

public class LeaveMeetingPush extends CommandOut {

	public String accountSid;
	
	public LeaveMeetingPush() {
		super(Command.LEAVE_MEETING_PUSH);
	}

	
}
