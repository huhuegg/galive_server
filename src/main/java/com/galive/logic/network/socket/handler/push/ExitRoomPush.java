package com.galive.logic.network.socket.handler.push;

import com.galive.logic.network.protocol.Command;
import com.galive.logic.network.protocol.CommandOut;

public class ExitRoomPush extends CommandOut {

	public String accountSid;
	
	public ExitRoomPush() {
		super(Command.ROOM_EXIT_PUSH);
	}

	
}
