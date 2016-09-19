package com.galive.logic.network.socket.handler;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Meeting;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.MeetingService;

@SocketRequestHandler(desc = "消息转发", command = Command.TRANSMIT)
public class TransmitHandler extends SocketBaseHandler {

	private MeetingService meetingService;

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--MeetingTransmitHandler(消息转发)--");
		
		MeetingTransmitgIn in = JSON.parseObject(reqData, MeetingTransmitgIn.class);
		String targetSid = in.targetSid;
		
		
		
	
		CommandOut out = new CommandOut(Command.TRANSMIT);
		return out;
	}
	
	public class MeetingTransmitgIn {
		public String targetSid = "";
	}
	
}
