package com.galive.logic.network.socket.handler;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Meeting;
import com.galive.logic.model.MeetingOptions;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.MeetingService;

@SocketRequestHandler(desc = "创建会议", command = Command.MEETING_CREATE)
public class CreateMeetingHandler extends SocketBaseHandler {

	private MeetingService meetingService;

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--CreateMeetingHandler(创建会议)--");
		
		CreateMeetingIn in = JSON.parseObject(reqData, CreateMeetingIn.class);
		
		String name = in.name;
		boolean useOwnerRoom = in.useOwnerRoom;
		MeetingOptions options = in.options;
		
		appendLog("会议名称(name):" + name);
		appendLog("是否使用自己的会议id(useOwnerRoom):" + useOwnerRoom);
		appendLog("会议设置(options):" + options);
	
		Meeting meeting = meetingService.createMeeting(account, name, useOwnerRoom, options);
		
		CreateLiveOut out = new CreateLiveOut();
		out.meeting = meeting;
		return out;
	}
	
	public static class CreateMeetingIn {

		public String name;
		public boolean useOwnerRoom = false;
		public MeetingOptions options;
	}

	public static class CreateLiveOut extends CommandOut {

		public CreateLiveOut() {
			super(Command.MEETING_CREATE);
		}

		public Meeting meeting;
	}

}
