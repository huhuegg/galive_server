package com.galive.logic.network.socket.handler;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Meeting;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.MeetingService;
import com.galive.logic.service.MeetingServiceImpl;

@SocketRequestHandler(desc = "会议信息", command = Command.MEETING_INFO)
public class MeetingInfoHandler extends SocketBaseHandler {

	private MeetingService meetingService = new MeetingServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--MeetingInfoHandler(会议信息)--");
		
		MeetingInfoIn in = JSON.parseObject(reqData, MeetingInfoIn.class);
		
		String searchName = in.searchName;
		String displayName = in.displayName;
		String profile = in.profile;
		String password = in.password;
		String coverImage = in.coverImage;
		List<String> tags = in.tags;

		appendLog("会议标识(searchName):" + searchName);
		appendLog("显示名称(displayName):" + displayName);
		appendLog("简介(profile):" + profile);
		appendLog("密码(password):" + password);
		appendLog("封面图片(coverImage):" + coverImage);
		appendLog("标签(tags):" + tags);
		
		Meeting meeting = null;
		if (!StringUtils.isEmpty(searchName)) {
			meeting = meetingService.findMeeting(searchName, null, null);
		} else {
			meeting = meetingService.updateMeeting(account, displayName, profile, password, tags, coverImage);
		}
		

		MeetingInfoOut out = new MeetingInfoOut();
		out.meeting = meeting;
		return out;
	}
	
	public static class MeetingInfoIn {

		public String searchName;
		public String displayName;
		public String profile;
		public String password;
		public List<String> tags;
		public String coverImage;
		
	}
	
	public static class MeetingInfoOut extends CommandOut {

		public MeetingInfoOut() {
			super(Command.MEETING_INFO);
		}
		
		public Meeting meeting;
		
	}
	
}
