package com.galive.logic.network.socket.handler;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Meeting;
import com.galive.logic.model.MeetingMember;
import com.galive.logic.model.MeetingOptions;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.MeetingService;
import com.galive.logic.service.MeetingServiceImpl;

@SocketRequestHandler(desc = "开始会议", command = Command.MEETING_START)
public class StartMeetingHandler extends SocketBaseHandler {

	private MeetingService meetingService = new MeetingServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--CreateMeetingHandler(创建会议)--");
		
		CreateMeetingIn in = JSON.parseObject(reqData, CreateMeetingIn.class);
		
		MeetingOptions options = in.options;
		
		appendLog("会议设置(options):" + options);
	
		Meeting meeting = meetingService.createMeeting(account, options);
		
		List<MeetingMember> members = meetingService.listMeetingMembersWithDetailInfo(meeting);
		meeting.setMembers(members);
		
		CreateLiveOut out = new CreateLiveOut();
		out.meeting = meeting;
		return out;
	}
	
	public static class StartMeetingIn {
			
	}

	public static class StartMeetingOut extends CommandOut {

		public CreateLiveOut() {
			super(Command.MEETING_START);
		}

		public Meeting meeting;
	}

}
