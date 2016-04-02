package com.galive.logic.network.socket.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandIn;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.ClientTransmitPush;

@SocketRequestHandler(desc = "客户端转发", command = Command.TRANSMIT)
public class ClientTransmitHandler extends SocketBaseHandler {

	private static Logger logger = LoggerFactory.getLogger(ClientTransmitHandler.class);
	
	@Override
	public CommandOut commandProcess(String userSid, String reqData) {
		logger.debug("客户端转发|" + userSid + "|" + reqData);
		ClientTransmitIn in = JSON.parseObject(reqData, ClientTransmitIn.class);
	
		// 推送至对方
		ClientTransmitPush push = new ClientTransmitPush();
		push.content = in.content;
		push.senderSid = userSid;
		pushMessage(in.to, push);
		
		CommandOut out = new CommandOut(Command.TRANSMIT);
		return out;
	}
	
	public static class ClientTransmitIn extends CommandIn {
		public String to; 
		public String content = "";
	}
	
}
