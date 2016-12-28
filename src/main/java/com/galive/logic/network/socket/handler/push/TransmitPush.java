package com.galive.logic.network.socket.handler.push;

import com.galive.logic.protocol.Command;
import com.galive.logic.protocol.CommandOut;


public class TransmitPush extends CommandOut {
	
	public String sender;
	
	public String content;
	
	public TransmitPush() {
		super(Command.TRANSMIT_PUSH);
	}

	
}
