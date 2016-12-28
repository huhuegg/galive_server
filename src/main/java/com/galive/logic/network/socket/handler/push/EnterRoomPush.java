package com.galive.logic.network.socket.handler.push;

import com.galive.logic.network.protocol.Command;
import com.galive.logic.network.protocol.CommandOut;

public class EnterRoomPush extends CommandOut {

	public String accountSid;
	
	public EnterRoomPush() {
		super(Command.ROOM_ENTER_PUSH);
	}

	
}
