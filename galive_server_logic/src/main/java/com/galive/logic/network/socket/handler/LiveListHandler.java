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
import com.galive.logic.model.Live;
import com.galive.logic.network.model.RespLive;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.LiveService;
import com.galive.logic.service.LiveServiceImpl;
import com.galive.logic.service.LoggerService;
import com.galive.logic.service.LoggerServiceImpl;

@SocketRequestHandler(desc = "直播间列表", command = Command.LIVE_LIST)
public class LiveListHandler extends SocketBaseHandler  {

	private static Logger logger = LoggerFactory.getLogger(LiveListHandler.class);
	
	private LiveService liveService = new LiveServiceImpl(logBuffer);
	private LoggerService loggerService = new LoggerServiceImpl();
	
	@Override
	public String handle(String userSid, String reqData) {
		try {
			LoggerHelper.appendLog("--获取直播列表--", logBuffer);
			PageParams in = JSON.parseObject(reqData, PageParams.class);
			
			List<Live> lives = liveService.listByLatestLiveTime(in.index, in.size);
			List<RespLive> respLives = new ArrayList<>();
			for (Live live : lives) {
				RespLive respLive = new RespLive();
				respLive.convert(live);
				respLives.add(respLive);
			}
			PageCommandOut<RespLive> out = new PageCommandOut<>(Command.LIVE_LIST, in);
			out.setData(respLives);
			String resp = out.socketResp();
			LoggerHelper.appendLog("响应客户端:" + resp, logBuffer);
			LoggerHelper.appendSplit(logBuffer);
			String logicLog = LoggerHelper.loggerString(logBuffer);
			logger.info(logicLog);
			loggerService.saveLogicLog(logicLog);
			return out.socketResp();
		} catch (Exception e) {
			String resp = respFail(null);
			LoggerHelper.appendLog("发生错误:" + e.getMessage(), logBuffer);
			LoggerHelper.appendLog("响应客户端:" + resp, logBuffer);
			LoggerHelper.appendSplit(logBuffer);
			String logicLog = LoggerHelper.loggerString(logBuffer);
			logger.error(logicLog);
			loggerService.saveLogicLog(logicLog);
			return resp;
		}	
	}
	
	private String respFail(String message) {
		String resp = CommandOut.failureOut(Command.LIVE_LIST, message).httpResp();
		return resp;
	}
}
