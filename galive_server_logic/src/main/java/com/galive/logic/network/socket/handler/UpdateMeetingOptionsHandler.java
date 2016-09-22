package com.galive.logic.network.socket.handler;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Meeting;
import com.galive.logic.model.MeetingMemberOptions;
import com.galive.logic.model.MeetingOptions;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.AccountService;
import com.galive.logic.service.AccountServiceImpl;
import com.galive.logic.service.MeetingService;
import com.galive.logic.service.MeetingServiceImpl;

@SocketRequestHandler(desc = "修改会议设置", command = Command.UPDATE_MEETING_OPTIONS)
public class UpdateMeetingOptionsHandler extends SocketBaseHandler {
	
	public enum UpdateMeetingOptionsType {
		
	}

	private MeetingService meetingService = new MeetingServiceImpl();
	private AccountService accountService = new AccountServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--UpdateMeetingOptionsHandler(修改会议设置)--");
		
		UpdateMeetingOptionsIn in = JSON.parseObject(reqData, UpdateMeetingOptionsIn.class);
		UpdateMeetingOptionsType type = in.type;
		/*appendLog("会议id(meetingSid):" + meetingSid);
		MeetingMemberOptions meetingMemberOptions = in.meetingMemberOptions;
		appendLog("会议成员设置(meetingMemberOptions):" + meetingMemberOptions);*/
		
		
	
		CommandOut out = new CommandOut(Command.UPDATE_MEETING_OPTIONS);
		return out;
	}
	
	public static class UpdateMeetingOptionsIn {

		public UpdateMeetingOptionsType type;
		public MeetingOptions options;
		public MeetingMemberOptions meetingMemberOptions;
		public boolean belongToMe = false;
		
	}
	
	public static class UpdateMeetingOptionsOut extends CommandOut {

		public UpdateMeetingOptionsOut() {
			super(Command.UPDATE_MEETING_OPTIONS);
		}

		public Meeting meeting;
	}
	
}
