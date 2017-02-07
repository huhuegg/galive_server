package com.galive.logic.network.socket.handler;

import com.alibaba.fastjson.JSON;
import com.galive.logic.model.Room;
import com.galive.logic.network.protocol.Command;
import com.galive.logic.network.protocol.CommandOut;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.RemoteClientService;
import com.galive.logic.service.RemoteClientServiceImpl;
import com.galive.logic.service.RoomService;
import com.galive.logic.service.RoomServiceImpl;
import org.apache.commons.lang.StringUtils;

@SocketRequestHandler(desc = "停止录屏", command = Command.STOP_RECORD)
public class StopRecordHandler extends WebSocketBaseHandler {

	private RoomService roomService = new RoomServiceImpl();
	private RemoteClientService remoteClientService = new RemoteClientServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--BindPCClientHandler(绑定PC端)--");
		Room room = roomService.findRoom(RoomService.FindRoomBy.Owner, account);
		if (room != null) {
			String clientId = room.getRemoteClientId();
			if (!StringUtils.isEmpty(clientId)) {
				remoteClientService.stopRecord(clientId);
			}
		}

		return new CommandOut(Command.STOP_RECORD);
	}
}
