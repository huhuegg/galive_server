package com.galive.logic.network.socket.handler;

import java.util.List;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Meeting;
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

		meetingService.updateShareState(account, true);

		Meeting meeting = meetingService.findMeeting(null, null, account);
		
		if (meeting != null) {
			ShareStartPush push = new ShareStartPush();
			String pushContent = push.socketResp();
			List<String> members = meeting.getMemberSids();
			for (String m : members) {
				if (!m.equals(account)) {
					pushMessage(m, pushContent);
					appendLog("推送房间内成员:" + m + " " + pushContent);
				}
			}
		}
		
		CommandOut out = new CommandOut(Command.SHARE_START);
		return out;

	}
}
