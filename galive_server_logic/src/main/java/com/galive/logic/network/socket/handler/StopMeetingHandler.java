package com.galive.logic.network.socket.handler;

import java.util.List;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Meeting;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.StopMeetingPush;
import com.galive.logic.service.MeetingService;
import com.galive.logic.service.MeetingServiceImpl;

@SocketRequestHandler(desc = "结束会议", command = Command.MEETING_STOP)
public class StopMeetingHandler extends SocketBaseHandler {

	private MeetingService meetingService = new MeetingServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--DestroyMeetingHandler(结束会议)--");
		
		Meeting meeting = meetingService.findMeeting(null, account, null);
		meetingService.stopMeeting(account);
		
		
		StopMeetingPush push = new StopMeetingPush();
		String pushContent = push.socketResp();

		List<String> members = meeting.getMemberSids();
		for (String member : members) {
			meetingService.leaveMeeting(member);
			if (!member.equals(account)) {
				pushMessage(member, pushContent);
				appendLog("推送房间内成员:" + member + " " + pushContent);
			}
		}
		
		CommandOut out = new CommandOut(Command.MEETING_STOP);
		return out;
	}
	
}
