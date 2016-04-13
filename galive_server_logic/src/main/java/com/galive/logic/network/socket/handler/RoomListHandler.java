package com.galive.logic.network.socket.handler;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.common.protocol.PageParams;
import com.galive.common.protocol.PageCommandOut;
import com.galive.logic.helper.LoggerHelper;
import com.galive.logic.model.Room;
import com.galive.logic.network.model.RespRoom;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.LoggerService;
import com.galive.logic.service.LoggerServiceImpl;
import com.galive.logic.service.RoomService;
import com.galive.logic.service.RoomServiceImpl;

@SocketRequestHandler(desc = "房间列表", command = Command.ROOM_LIST)
public class RoomListHandler extends SocketBaseHandler  {

	private static Logger logger = LoggerFactory.getLogger(RoomListHandler.class);
	
	private RoomService roomService = new RoomServiceImpl(logBuffer);
	private LoggerService loggerService = new LoggerServiceImpl();
	
	@Override
	public String handle(String userSid, String reqData) {
		try {
			LoggerHelper.appendLog("--获取房间列表--", logBuffer);
			PageParams in = JSON.parseObject(reqData, PageParams.class);
			
			List<Room> rooms = roomService.listByCreateTime(in.index, in.size);
			List<RespRoom> respRooms = new ArrayList<>();
			for (Room r : rooms) {
				RespRoom rr = RespRoom.convert(r);
				respRooms.add(rr);
			}
			PageCommandOut<RespRoom> out = new PageCommandOut<>(Command.ROOM_LIST, in);
			out.setData(respRooms);
			String resp = out.socketResp();
			LoggerHelper.appendLog("响应客户端|" + resp, logBuffer);
			LoggerHelper.appendSplit(logBuffer);
			String logicLog = LoggerHelper.loggerString(logBuffer);
			logger.info(logicLog);
			loggerService.saveLogicLog(logicLog);
			return out.socketResp();
		} catch (Exception e) {
			String resp = respFail(null);
			LoggerHelper.appendLog("响应客户端|" + resp, logBuffer);
			LoggerHelper.appendSplit(logBuffer);
			String logicLog = LoggerHelper.loggerString(logBuffer);
			logger.error(logicLog);
			loggerService.saveLogicLog(logicLog);
			return resp;
		}	
	}
	
	private String respFail(String message) {
		String resp = CommandOut.failureOut(Command.ROOM_LIST, message).httpResp();
		logger.error("获取房间列表失败|" + resp);
		return resp;
	}
}
