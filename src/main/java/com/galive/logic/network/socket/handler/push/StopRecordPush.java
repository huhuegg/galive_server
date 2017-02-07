package com.galive.logic.network.socket.handler.push;

import com.galive.logic.network.protocol.Command;
import com.galive.logic.network.protocol.CommandOut;

public class StopRecordPush extends CommandOut {

	public StopRecordPush() {
		super(Command.STOP_RECORD_PUSH);
	}

	
}
