package com.galive.logic.handler.push;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;

public class RoomSDPPush extends CommandOut {

	public int type;
	public String sdp;
	public String senderSid;
	
	public RoomSDPPush() {
		super(Command.ROOM_SDP_PUSH);
	}
}
