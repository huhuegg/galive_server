package com.galive.logic.network.socket.handler.push;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.network.model.RespLive;

public class LiveStopPush extends CommandOut {

	public RespLive live;
	
	public LiveStopPush() {
		super(Command.LIVE_STOP_PUSH);
	}
}
