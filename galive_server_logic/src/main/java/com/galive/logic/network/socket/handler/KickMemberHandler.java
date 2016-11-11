package com.galive.logic.network.socket.handler;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Meeting;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.KickMeetingMemberPush;
import com.galive.logic.service.MeetingService;
import com.galive.logic.service.MeetingServiceImpl;

@SocketRequestHandler(desc = "踢出会议成员", command = Command.MEETING_MEMBER_KICK)
public class KickMemberHandler extends SocketBaseHandler {

	private MeetingService meetingService = new MeetingServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--KickMemberHandler(踢出会议成员)--");
		
		KickMemberIn in = JSON.parseObject(reqData, KickMemberIn.class);
		String targetSid = in.targetSid;
		appendLog("目标id(targetSid):" + targetSid);
		
		Meeting meeting = meetingService.kickMember(account, targetSid);
	
		KickMeetingMemberPush push = new KickMeetingMemberPush();
		push.targetSid = targetSid;
		String pushContent = push.socketResp();
		List<String> members = meeting.getMemberSids();
		for (String m : members) {
			if (!m.equals(account)) {
				pushMessage(m, pushContent);
				appendLog("推送房间内成员:" + m + " " + pushContent);
			}
		}
		
		CommandOut out = new CommandOut(Command.MEETING_MEMBER_KICK);
		return out;
	}
	
	public static class KickMemberIn {

		public String targetSid;
		
	}
}
