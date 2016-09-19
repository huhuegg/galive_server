package com.galive.logic.network.socket.handler;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.network.socket.SocketRequestHandler;

@SocketRequestHandler(desc = "消息转发", command = Command.TRANSMIT)
public class TransmitHandler extends SocketBaseHandler {

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--MeetingTransmitHandler(消息转发)--");
		
		MeetingTransmitgIn in = JSON.parseObject(reqData, MeetingTransmitgIn.class);
		List<String> targetSids = in.targetSids;
		String content = in.content;
		
		for (String s : targetSids) {
			pushMessage(s, content);
		}
	
		CommandOut out = new CommandOut(Command.TRANSMIT);
		return out;
	}
	
	public class MeetingTransmitgIn {
		public List<String> targetSids = new ArrayList<String>();
		public String content = "";
	}
	
}
