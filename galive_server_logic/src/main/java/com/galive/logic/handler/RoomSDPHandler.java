package com.galive.logic.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandIn;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.handler.push.RoomSDPPush;
import com.galive.logic.model.Room;

@LogicHandler(desc = "客户端发送sdp", command = Command.ROOM_SDP)
public class RoomSDPHandler extends BaseHandler {

	private static Logger logger = LoggerFactory.getLogger(RoomSDPHandler.class);
	
	@Override
	public CommandOut commandProcess(String userSid, String reqData) {
		logger.debug("发送sdp|" + userSid + "|" + reqData);
		RoomSDPCommandIn in = JSON.parseObject(reqData, RoomSDPCommandIn.class);
		Room room = Room.findRoomByUser(userSid);
		if (room == null) {
			CommandOut out = CommandOut.failureOut(Command.ROOM_SDP, "您不在房间中");
			return out;
		}
		
		// 推送至对方
		RoomSDPPush push = new RoomSDPPush();
		push.sdp = in.sdp;
		logger.debug("======sdp======");
		logger.debug("======" + in.sdp + "======");
		logger.debug("===============");
		push.senderSid = userSid;
		pushMessage(in.receiverSid, push);
		
		CommandOut out = new CommandOut(Command.ROOM_SDP);
		return out;
	}
	
	public static class RoomSDPCommandIn extends CommandIn {
		public String sdp = "";
		public String receiverSid = "";
	}

}
