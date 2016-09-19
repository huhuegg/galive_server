package com.galive.logic.network.socket.handler;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.MeetingService;

@SocketRequestHandler(desc = "结束会议", command = Command.MEETING_DESTROY)
public class DestroyMeetingHandler extends SocketBaseHandler {

	private MeetingService meetingService;

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--DestroyMeetingHandler(结束会议)--");
		
		meetingService.destroyMeeting(account);
	
		CommandOut out = new CommandOut(Command.MEETING_DESTROY);
		return out;
	}
	
}
