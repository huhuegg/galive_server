package com.galive.logic.network.socket.handler.push;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;

public class StopMeetingPush extends CommandOut {
	
	public StopMeetingPush() {
		super(Command.STOP_MEETING_PUSH);
	}

	
}
