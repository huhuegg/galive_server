package com.galive.logic.network.socket.handler.push;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;

public class RoomRefreshPush extends CommandOut {

	public RoomRefreshPush() {
		super(Command.ROOM_REFRESH_PUSH);
	}
}
