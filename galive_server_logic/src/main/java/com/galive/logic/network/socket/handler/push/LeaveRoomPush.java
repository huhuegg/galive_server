package com.galive.logic.network.socket.handler.push;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;

public class LeaveRoomPush extends CommandOut {

	public String accountSid;
	
	public LeaveRoomPush() {
		super(Command.ROOM_LEAVE_PUSH);
	}

	
}
