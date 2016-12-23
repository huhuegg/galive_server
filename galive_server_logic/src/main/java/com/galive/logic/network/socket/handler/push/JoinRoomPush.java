package com.galive.logic.network.socket.handler.push;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;

public class JoinRoomPush extends CommandOut {

	public String accountSid;
	
	public JoinRoomPush() {
		super(Command.ROOM_JOIN_PUSH);
	}

	
}
