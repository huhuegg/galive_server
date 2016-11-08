package com.galive.logic.network.socket.handler;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Meeting;
import com.galive.logic.model.account.Account;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.JoinMeetingPush;
import com.galive.logic.service.AccountService;
import com.galive.logic.service.AccountServiceImpl;
import com.galive.logic.service.MeetingService;
import com.galive.logic.service.MeetingServiceImpl;

@SocketRequestHandler(desc = "加入会议", command = Command.MEETING_JOIN)
public class JoinMeetingHandler extends SocketBaseHandler {

	private MeetingService meetingService = new MeetingServiceImpl();
	private AccountService accountService = new AccountServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--JoinMeetingHandler(加入会议)--");
		
		JoinMeetingIn in = JSON.parseObject(reqData, JoinMeetingIn.class);
		String searchName = in.searchName;
		appendLog("会议id(searchName):" + searchName);
		
		String password = in.password;
		appendLog("会议密码(password):" + password);
		
		Meeting meeting;
		if (in.preJoin) {
			meeting = meetingService.joinMeeting(account, searchName, password);
			JoinMeetingOut out = new JoinMeetingOut();
			out.meeting = meeting;
			return out;
		} else {
			Meeting jMeeting = meetingService.findMeeting(searchName, null, null);
			jMeeting = meetingService.joinMeeting(account, jMeeting.getSid(), password);
			meeting = meetingService.findMeeting(searchName, null, null);
			Account act = accountService.findAndCheckAccount(account);
			JoinMeetingPush push = new JoinMeetingPush();
			push.account = act;
			String pushContent = push.socketResp();
			List<String> members = meeting.getMemberSids();
			for (String m : members) {
				if (!m.equals(account)) {
					pushMessage(m, pushContent);
					appendLog("推送房间内成员:" + m + " " + pushContent);
				}
			}
			CommandOut out = new CommandOut(Command.MEETING_JOIN);
			return out;
		}
	}
	
	public static class JoinMeetingIn {

		public String searchName = "";
		public String password = "";
		public boolean preJoin = false;
		
	}
	
	public static class JoinMeetingOut extends CommandOut {

		public JoinMeetingOut() {
			super(Command.MEETING_JOIN);
		}
		
		public Meeting meeting;
		
	}
	
}
