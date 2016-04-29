package com.galive.logic.network.socket.handler;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.TransmitPush;

@SocketRequestHandler(desc = "客户端转发", command = Command.TRANSMIT)
public class TransmitHandler extends SocketBaseHandler {
	
	@Override
	public CommandOut handle(String userSid, String reqData) throws Exception {
		ClientTransmitIn in = JSON.parseObject(reqData, ClientTransmitIn.class);

		TransmitPush push = new TransmitPush();
		push.content = in.content;
		push.senderSid = userSid;
		String transmitContent = push.socketResp();
		pushMessage(in.to, transmitContent);
		
		CommandOut out = new CommandOut(Command.TRANSMIT);
		return out;
	}
	
	public static class ClientTransmitIn {
		public String to; 
		public String content = "";
	}
	
	
}
