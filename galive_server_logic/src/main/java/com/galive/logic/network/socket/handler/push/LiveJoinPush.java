package com.galive.logic.network.socket.handler.push;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.network.model.RespUser;

public class LiveJoinPush extends CommandOut {

	public RespUser user;
	
	public LiveJoinPush() {
		super(Command.LIVE_JOIN_PUSH);
	}
}
