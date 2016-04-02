package com.galive.logic.handler.push;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.network.model.RespUser;

public class UserOnlinePush extends CommandOut {

	public RespUser user;
	
	public UserOnlinePush() {
		super(Command.USR_ONLINE_PUSH);
	}

	
}
