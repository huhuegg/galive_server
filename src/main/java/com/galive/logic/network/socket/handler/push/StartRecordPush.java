package com.galive.logic.network.socket.handler.push;

import com.galive.logic.network.protocol.Command;
import com.galive.logic.network.protocol.CommandOut;

public class StartRecordPush extends CommandOut {

	public StartRecordPush() {
		super(Command.START_RECORD_PUSH);
	}

	
}
