package com.galive.logic.network.socket.handler;

import java.util.List;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Meeting;
import com.galive.logic.model.MeetingMember;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.ShareStartPush;
import com.galive.logic.service.MeetingService;
import com.galive.logic.service.MeetingServiceImpl;

@SocketRequestHandler(desc = "开始分享", command = Command.SHARE_START)
public class ShareStartHandler extends SocketBaseHandler {

	private MeetingService meetingService = new MeetingServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--ShareStartHandler(开始分享)--");

		meetingService.changeShareState(account, true);

		Meeting meeting = meetingService.leaveMeeting(account);
		
		ShareStartPush push = new ShareStartPush();
		push.accountSid = account;
		String pushContent = push.socketResp();
		List<MeetingMember> members = meeting.getMembers();
		for (MeetingMember m : members) {
			if (!m.getAccountSid().equals(account)) {
				pushMessage(m.getAccountSid(), pushContent);
				appendLog("推送房间内成员:" + m.getAccountSid() + " " + pushContent);
			}
		}
		
		CommandOut out = new CommandOut(Command.SHARE_START);
		return out;

	}
}
