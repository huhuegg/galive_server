package com.galive.logic.network.socket.handler.push;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;

public class RoomExitPush extends CommandOut {

	public String userSid;
	public boolean refuseInvite = false;
	
	public RoomExitPush() {
		super(Command.ROOM_EXIT_PUSH);
	}

	
}
