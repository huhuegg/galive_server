package com.galive.logic.network.socket.handler.push;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;

public class ExitRoomPush extends CommandOut {

	public String accountSid;
	
	public ExitRoomPush() {
		super(Command.ROOM_EXIT_PUSH);
	}

	
}
