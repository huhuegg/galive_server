package com.galive.logic.network.socket.handler.push;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;

public class ClientTransmitPush extends CommandOut {

	public String senderSid;
	public String content;
	
	public ClientTransmitPush() {
		super(Command.TRANSMIT_PUSH);
	}
}
