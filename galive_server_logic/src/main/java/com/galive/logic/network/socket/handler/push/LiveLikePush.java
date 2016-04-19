package com.galive.logic.network.socket.handler.push;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.network.model.RespUser;

public class LiveLikePush extends CommandOut {

	public RespUser user;
	
	public LiveLikePush() {
		super(Command.LIVE_LIKE_PUSH);
	}
}
