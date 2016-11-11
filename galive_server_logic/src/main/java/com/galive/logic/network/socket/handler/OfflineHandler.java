package com.galive.logic.network.socket.handler;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Meeting;
import com.galive.logic.model.MeetingMember;
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
		
		
		Meeting meeting = meetingService.findMeeting(null, account, false);
		if (meeting != null) {
			
			boolean live = false;
			for (MeetingMember m : meeting.getMembers()) {
				if (ChannelManager.getInstance().isOnline(m.getAccountSid())) {
					live = true;
					break;
				}
			}
			if (!live) {
				meetingService.destroyMeeting(meeting.getHolder());
			}
		}
		// 清除直播信息
//		liveService.clearLiveForAccount(account);
//		CommandOut out = new CommandOut(Command.OFFLINE);
//		return out;
		
		return null;
	}

}
