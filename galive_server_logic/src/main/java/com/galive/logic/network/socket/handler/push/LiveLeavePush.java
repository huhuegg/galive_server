package com.galive.logic.network.socket.handler.push;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.network.model.RespUser;

public class LiveLeavePush extends CommandOut {

	public RespUser user;
	
	public LiveLeavePush() {
		super(Command.LIVE_LEAVE_PUSH);
	}
}
