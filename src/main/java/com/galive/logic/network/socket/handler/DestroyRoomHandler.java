package com.galive.logic.network.socket.handler;

import java.util.Set;

import com.galive.logic.model.Room;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.DestroyRoomPush;
import com.galive.logic.network.protocol.Command;
import com.galive.logic.network.protocol.CommandOut;
import com.galive.logic.service.RemoteClientService;
import com.galive.logic.service.RemoteClientServiceImpl;
import com.galive.logic.service.RoomService;
import com.galive.logic.service.RoomServiceImpl;
import org.apache.commons.lang.StringUtils;

@SocketRequestHandler(desc = "销毁房间", command = Command.ROOM_DESTROY)
public class DestroyRoomHandler extends WebSocketBaseHandler {

	private RoomService roomService = new RoomServiceImpl();
	private RemoteClientService remoteClientService = new RemoteClientServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--DestroyRoomHandler(销毁房间)--");

		Room room = roomService.findRoom(RoomService.FindRoomBy.Owner, account);
		if (room != null) {
			String clientId = room.getRemoteClientId();
			if (!StringUtils.isEmpty(clientId)) {
				remoteClientService.unbound(clientId);
			}

			room = roomService.destroyRoom(account);
			if (room != null) {
				Set<String> members = room.getMembers();
				DestroyRoomPush push = new DestroyRoomPush();
				String pushContent = push.socketResp();
				for (String member : members) {
					if (!member.equals(account)) {
						1roomService.leaveRoom(member);
						pushMessage(member, pushContent);
						appendLog("推送房间内成员:" + member + " " + pushContent);
					}
				}

			}
		}


		return new CommandOut(Command.ROOM_DESTROY);
	}
	
}
