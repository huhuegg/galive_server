package com.galive.logic.handler.push;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.network.model.RespUser;

public class UserOfflinePush extends CommandOut {

	public RespUser user;
	
	public UserOfflinePush() {
		super(Command.USR_OFFLINE_PUSH);
	}

	
}
