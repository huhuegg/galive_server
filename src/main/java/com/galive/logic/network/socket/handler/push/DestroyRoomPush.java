package com.galive.logic.network.socket.handler.push;

import com.galive.logic.protocol.Command;
import com.galive.logic.protocol.CommandOut;

public class DestroyRoomPush extends CommandOut {
	
	public DestroyRoomPush() {
		super(Command.ROOM_DESTROY_PUSH);
	}

	
}
