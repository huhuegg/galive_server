package com.galive.logic.network.socket.handler;

import java.util.List;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Meeting;
import com.galive.logic.network.socket.ChannelManager;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.MeetingService;
import com.galive.logic.service.MeetingServiceImpl;

@SocketRequestHandler(desc = "用户下线", command = Command.OFFLINE)
public class OfflineHandler extends SocketBaseHandler {

	private MeetingService meetingService = new MeetingServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--OnlineHandler(用户下线)--");

		Meeting meeting = meetingService.findMeeting(null, null, account);
		if (meeting != null) {
			List<String> memberSids = meeting.getMemberSids();
			boolean live = false;
			for (String s : memberSids) {
				if (ChannelManager.getInstance().isOnline(s)) {
					live = true;
					break;
				}
			}
			if (!live) {
				// 清除房间信息
				appendLog("会议成员不在线, 清楚会议状态");
			}
			meetingService.stopMeeting(meeting.getAccountSid());
		}
	
		return null;
	}

}
