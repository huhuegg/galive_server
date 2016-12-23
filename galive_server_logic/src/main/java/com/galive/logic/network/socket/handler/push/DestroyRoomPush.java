package com.galive.logic.network.socket.handler.push;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;

public class DestroyRoomPush extends CommandOut {
	
	public DestroyRoomPush() {
		super(Command.ROOM_DESTROY_PUSH);
	}

	
}
