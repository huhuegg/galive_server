package com.galive.logic.network.socket.handler;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Meeting;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.MeetingService;
import com.galive.logic.service.MeetingServiceImpl;

@SocketRequestHandler(desc = "会议列表", command = Command.MEETING_LIST)
public class MeetingListHandler extends SocketBaseHandler {

	private MeetingService meetingService = new MeetingServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--MeetingListHandler(会议列表)--");
		
		MeetingListIn in = JSON.parseObject(reqData, MeetingListIn.class);
		int type = in.type;
		int index = in.index;
		int size = in.size;
		appendLog("type(type):" + type);
		appendLog("index(index):" + index);
		appendLog("size(size):" + size);
		
		List<Meeting> meetings = null;
		if (type == 0) {
			meetings = meetingService.listMeetings(index, index + size - 1);
		} else {
			meetings = meetingService.listStartedMeetings(index, index + size - 1);
		}
		
		MeetingListOut out = new MeetingListOut();
		out.meetings = meetings;
		out.type = type;
		out.index = index;
		out.size = size;
		return out;
	}
	
	public static class MeetingListIn {

		public int type = 0;
		public int index = 0;
		public int size = 10;
		
	}
	
	public static class MeetingListOut extends CommandOut {

		public MeetingListOut() {
			super(Command.MEETING_LIST);
		}
		
		public List<Meeting> meetings;
		
		public int type = 0;
		public int index = 0;
		public int size = 10;
		
	}
	
}
