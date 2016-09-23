package com.galive.logic.network.socket.handler.push;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.account.Account;

public class JoinMeetingPush extends CommandOut {

	public Account account;
	
	public JoinMeetingPush() {
		super(Command.JOIN_MEETING_PUSH);
	}

	
}
