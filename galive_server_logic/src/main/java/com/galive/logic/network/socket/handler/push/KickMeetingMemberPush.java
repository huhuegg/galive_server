package com.galive.logic.network.socket.handler.push;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;

public class KickMeetingMemberPush extends CommandOut {
	
	public String targetSid;
	
	public KickMeetingMemberPush() {
		super(Command.MEETING_MEMBER_KICK_PUSH);
	}

	
}
