package com.galive.logic.network.socket.handler;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.MeetingService;

@SocketRequestHandler(desc = "加入会议", command = Command.MEETING_LEAVE)
public class LeaveMeetingHandler extends SocketBaseHandler {

	private MeetingService meetingService;

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--LeaveMeetingHandler(离开会议)--");
		
		
		meetingService.leaveMeeting(account);
	
		CommandOut out = new CommandOut(Command.MEETING_LEAVE);
		return out;
	}
	
	
}
