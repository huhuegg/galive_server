package com.galive.logic.network.socket.handler;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.MeetingMemberOptions;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.MeetingService;

@SocketRequestHandler(desc = "加入会议", command = Command.MEETING_JOIN)
public class JoinMeetingHandler extends SocketBaseHandler {

	private MeetingService meetingService;

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--JoinMeetingHandler(加入会议)--");
		
		JoinMeetingIn in = JSON.parseObject(reqData, JoinMeetingIn.class);
		String meetingSid = in.meetingSid;
		appendLog("会议id(meetingSid):" + meetingSid);
		MeetingMemberOptions meetingMemberOptions = in.meetingMemberOptions;
		appendLog("会议成员设置(meetingMemberOptions):" + meetingMemberOptions);
		
		meetingService.joinMeeting(account, meetingSid, meetingMemberOptions);
	
		CommandOut out = new CommandOut(Command.MEETING_JOIN);
		return out;
	}
	
	public static class JoinMeetingIn {

		public String meetingSid;
		public MeetingMemberOptions meetingMemberOptions;
		
	}
	
}
