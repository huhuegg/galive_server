package com.galive.logic.handler.push;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;

public class ClientTransmitPush extends CommandOut {

	public String senderSid;
	public String content;
	
	public ClientTransmitPush() {
		super(Command.CLIENT_TRANSMIT_PUSH);
	}
}
