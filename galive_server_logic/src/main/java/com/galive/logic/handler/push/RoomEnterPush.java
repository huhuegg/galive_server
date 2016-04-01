package com.galive.logic.handler.push;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.handler.model.RespUser;

public class RoomEnterPush extends CommandOut {

	public RespUser user;
	
	public RoomEnterPush() {
		super(Command.ROOM_ENTER_PUSH);
	}

	
}
