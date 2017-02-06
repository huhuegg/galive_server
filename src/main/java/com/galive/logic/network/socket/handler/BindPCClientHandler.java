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

@SocketRequestHandler(desc = "绑定PC端", command = Command.BIND_PC_CLIENT)
public class BindPCClientHandler extends WebSocketBaseHandler {

	private RoomService roomService = new RoomServiceImpl();
	private RemoteClientService remoteClientService = new RemoteClientServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--BindPCClientHandler(绑定PC端)--");
		BindPCClientIn in = JSON.parseObject(reqData, BindPCClientIn.class);
		String clientId = in.clientId;
		appendLog("pc id(clientId):" + clientId);
		Room room = roomService.bindPCClient(account, clientId);

		String publishUrl = RemoteClientService.PUBLISH_URL;

		// 通知pc端
		remoteClientService.bind(clientId, publishUrl);

		return new CommandOut(Command.BIND_PC_CLIENT);
	}

	public static class BindPCClientIn {

		public String clientId = "";

	}


}
