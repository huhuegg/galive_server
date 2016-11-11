package com.galive.logic.network.socket.handler;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Meeting;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.MeetingService;
import com.galive.logic.service.MeetingServiceImpl;

@SocketRequestHandler(desc = "开始会议", command = Command.MEETING_START)
public class StartMeetingHandler extends SocketBaseHandler {

	private MeetingService meetingService = new MeetingServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--StartMeetingHandler(开始会议)--");
		
		Meeting meeting = meetingService.startMeeting(account);
	
		StartMeetingOut out = new StartMeetingOut();
		out.meeting = meeting;
		return out;
	}
	
	public static class StartMeetingOut extends CommandOut {

		public StartMeetingOut() {
			super(Command.MEETING_START);
		}

		public Meeting meeting;
	}

}
