package com.galive.logic.network.socket.handler;

import java.util.Set;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Room;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.ScreenShareStartPush;
import com.galive.logic.service.RoomService;
import com.galive.logic.service.RoomServiceImpl;

@SocketRequestHandler(desc = "开始分享", command = Command.SCREEN_SHARE_START)
public class ShareStartHandler extends SocketBaseHandler {

	private RoomService roomService = new RoomServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--ShareStartHandler(开始分享)--");

		

		Room room = roomService.updateScreenShareState(account, true);
		
		if (room != null) {

			ScreenShareStartPush push = new ScreenShareStartPush();
			String pushContent = push.socketResp();
			Set<String> members = room.getMembers();
			for (String m : members) {
				if (!m.equals(account)) {
					pushMessage(m, pushContent);
					appendLog("推送房间内成员:" + m + " " + pushContent);
				}
			}
		}
		
		CommandOut out = new CommandOut(Command.SCREEN_SHARE_START);
		return out;

	}
}
