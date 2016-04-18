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
import com.galive.logic.model.User;
import com.galive.logic.network.model.RespUser;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.LiveService;
import com.galive.logic.service.LiveServiceImpl;
import com.galive.logic.service.LoggerService;
import com.galive.logic.service.LoggerServiceImpl;

@SocketRequestHandler(desc = "直播观众列表", command = Command.LIVE_AUDIENCE_LIST)
public class LiveAudienceListHandler extends SocketBaseHandler  {

	private static Logger logger = LoggerFactory.getLogger(LiveAudienceListHandler.class);
	
	private LiveService liveService = new LiveServiceImpl(logBuffer);
	private LoggerService loggerService = new LoggerServiceImpl();
	
	@Override
	public String handle(String userSid, String reqData) {
		try {
			LoggerHelper.appendLog("--直播观众列表--", logBuffer);
			LiveAudienceListIn in = JSON.parseObject(reqData, LiveAudienceListIn.class);
			
			List<User> users = liveService.listAudiences(in.liveSid, in.index, in.size);
			List<RespUser> respUsers = new ArrayList<>();
			for (User u : users) {
				RespUser respUser = new RespUser();
				respUser.convert(u);
				respUsers.add(respUser);
			}
			PageCommandOut<RespUser> out = new PageCommandOut<>(Command.LIVE_AUDIENCE_LIST, in);
			out.setData(respUsers);
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
	
	public static class LiveAudienceListIn extends PageParams {
		public String liveSid = "";
	}
	
	private String respFail(String message) {
		String resp = CommandOut.failureOut(Command.LIVE_AUDIENCE_LIST, message).httpResp();
		return resp;
	}
}
