package com.galive.logic.network.socket.handler.push;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;

public class DestroyMeetingPush extends CommandOut {
	
	public String accountSid;
	
	public DestroyMeetingPush() {
		super(Command.DESTROY_MEETING_PUSH);
	}

	
}
