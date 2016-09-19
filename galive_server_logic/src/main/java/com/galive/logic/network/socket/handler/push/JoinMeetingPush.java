package com.galive.logic.network.socket.handler.push;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.account.PlatformAccount;

public class JoinMeetingPush extends CommandOut {

	public PlatformAccount account;
	
	public JoinMeetingPush() {
		super(Command.JOIN_MEETING_PUSH);
	}

	
}
