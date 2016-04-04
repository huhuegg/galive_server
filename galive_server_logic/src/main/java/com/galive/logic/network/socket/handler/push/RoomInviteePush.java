package com.galive.logic.network.socket.handler.push;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.network.model.RespRoom;

public class RoomInviteePush extends CommandOut {

	public RespRoom room;
	
	public RoomInviteePush() {
		super(Command.ROOM_INVITEE_PUSH);
	}

	
}
