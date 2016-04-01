package com.galive.logic.handler.push;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;

public class RoomExitPush extends CommandOut {

	public String userSid;
	
	public RoomExitPush() {
		super(Command.ROOM_EXIT_PUSH);
	}

	
}
