package com.galive.logic.network.socket.handler;

import java.util.Set;

import com.galive.logic.model.Room;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.ExitRoomPush;
import com.galive.logic.network.protocol.Command;
import com.galive.logic.network.protocol.CommandOut;
import com.galive.logic.service.RemoteClientService;
import com.galive.logic.service.RemoteClientServiceImpl;
import com.galive.logic.service.RoomService;
import com.galive.logic.service.RoomServiceImpl;
import org.apache.commons.lang.StringUtils;

@SocketRequestHandler(desc = "离开房间", command = Command.ROOM_EXIT)
public class ExitRoomHandler extends WebSocketBaseHandler {

	private RoomService roomService = new RoomServiceImpl();
	private RemoteClientService remoteClientService = new RemoteClientServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--ExitRoomHandler(离开房间)--");

		Room room = roomService.leaveRoom(account);
		
		if (room != null) {
			ExitRoomPush push = new ExitRoomPush();
			push.accountSid = account;
			String pushContent = push.socketResp();
			Set<String> members = room.getMembers();
			for (String m : members) {
				if (!m.equals(account)) {
					pushMessage(m, pushContent);
					appendLog("推送房间内成员:" + m + " " + pushContent);
				}
			}
			String clientId = room.getRemoteClientId();
			if (!StringUtils.isEmpty(clientId)) {
				remoteClientService.unbound(clientId);
			}
		}

		return new CommandOut(Command.ROOM_EXIT);
	}
	
	
}
