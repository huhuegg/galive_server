package com.galive.logic.network.socket.handler;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Meeting;
import com.galive.logic.model.MeetingMemberOptions;
import com.galive.logic.model.MeetingOptions;
import com.galive.logic.model.account.Account;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.AccountService;
import com.galive.logic.service.AccountServiceImpl;
import com.galive.logic.service.MeetingService;
import com.galive.logic.service.MeetingServiceImpl;

@SocketRequestHandler(desc = "用户上线", command = Command.ONLINE)
public class OnlineHandler extends SocketBaseHandler {

	private MeetingService meetingService = new MeetingServiceImpl();
	private AccountService accountService = new AccountServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--OnlineHandler(用户上线)--");

		OnlineOut out = new OnlineOut();
		// 返回会议信息
		Meeting meeting = meetingService.findMeeting(null, account, false);
		if (meeting != null) {
			out.meeting = meeting;
		}
		
		Account act = accountService.findAndCheckAccount(account);
		MeetingOptions options = act.getMeetingOptions();
		if (options == null) {
			options = accountService.createMeetingOptions(account);
			act.setMeetingOptions(options);
		}
		
		MeetingMemberOptions meetingMemberOptions = act.getMeetingMemberOptions();
		if (meetingMemberOptions == null) {
			meetingMemberOptions = accountService.createMeetingMemberOptions(account);
			act.setMeetingMemberOptions(meetingMemberOptions);
		}
		accountService.saveOrUpdateAccount(act);
		out.meetingOptions = options;
		out.meetingMemberOptions = meetingMemberOptions;
		return out;
	}
	
	public class OnlineOut extends CommandOut {

		public Meeting meeting;
		public MeetingOptions meetingOptions;
		public MeetingMemberOptions meetingMemberOptions;
		
		public OnlineOut() {
			super(Command.ONLINE);
			
		}
		
	}

}
