package com.galive.logic.handler.push;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;

public class RoomRefreshPush extends CommandOut {

	public RoomRefreshPush() {
		super(Command.ROOM_REFRESH_PUSH);
	}
}
