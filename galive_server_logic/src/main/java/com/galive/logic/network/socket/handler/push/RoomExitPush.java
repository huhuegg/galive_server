package com.galive.logic.network.socket.handler.push;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;

public class RoomExitPush extends CommandOut {

	public static enum RoomExitType {
		ExitRoom,
		RefuseInvite,
		RefuseQuestionInvite
	}
	
	public String userSid;

	public int type;
	
	public RoomExitPush() {
		super(Command.ROOM_EXIT_PUSH);
	}

	
}
