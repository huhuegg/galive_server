package com.galive.logic.network.socket.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.exception.LogicException;
import com.galive.logic.helper.LoggerHelper;
import com.galive.logic.model.Live;
import com.galive.logic.model.User;
import com.galive.logic.network.model.RespLiveInfo;
import com.galive.logic.network.model.RespUser;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.LiveService;
import com.galive.logic.service.LiveServiceImpl;
import com.galive.logic.service.LoggerService;
import com.galive.logic.service.LoggerServiceImpl;
import com.galive.logic.service.UserService;
import com.galive.logic.service.UserServiceImpl;

@SocketRequestHandler(desc = "获取直播信息", command = Command.LIVE_INFO)
public class LiveInfoHandler extends SocketBaseHandler  {

	private static Logger logger = LoggerFactory.getLogger(LiveInfoHandler.class);

	private LiveService liveService = new LiveServiceImpl(logBuffer);
	private UserService userService = new UserServiceImpl(logBuffer);
	private LoggerService loggerService = new LoggerServiceImpl();
	
	@Override
	public String handle(String userSid, String reqData) {
		try {
			LoggerHelper.appendLog("--获取直播信息--", logBuffer);
			LiveInfoIn in = JSON.parseObject(reqData, LiveInfoIn.class);
			String liveSid = in.liveSid;
			Live live = liveService.findLive(liveSid);
			
			
			LiveInfoOut out = new LiveInfoOut();
			RespLiveInfo info = new RespLiveInfo();
			info.convert(live);
			long[] likeNums = liveService.likeNums(liveSid);
			info.likeNum = likeNums[0];
			info.likeAllNum = likeNums[1];
			User user = userService.findUserBySid(live.getOwnerSid());
			RespUser respUser = new RespUser();
			respUser.convert(user);
			info.presenter = respUser;
			out.live = info;
			String resp = out.socketResp();
			LoggerHelper.appendLog("响应客户端:" + resp, logBuffer);
			LoggerHelper.appendSplit(logBuffer);
			String logicLog = LoggerHelper.loggerString(logBuffer);
			logger.info(logicLog);
			loggerService.saveLogicLog(logicLog);
			return resp;
		} catch (LogicException e) {
			String resp = respFail(e.getMessage());
			LoggerHelper.appendLog("响应客户端:" + resp, logBuffer);
			LoggerHelper.appendSplit(logBuffer);
			String logicLog = LoggerHelper.loggerString(logBuffer);
			logger.error(logicLog);
			loggerService.saveLogicLog(logicLog);
			return resp;
		} catch (Exception e) {
			String resp = respFail(null);
			LoggerHelper.appendLog("发生错误" + e.getMessage(), logBuffer);
			LoggerHelper.appendLog("响应客户端|" + resp, logBuffer);
			LoggerHelper.appendSplit(logBuffer);
			String logicLog = LoggerHelper.loggerString(logBuffer);
			logger.error(logicLog);
			loggerService.saveLogicLog(logicLog);
			return resp;
		}
		
	}
	
	public static class LiveInfoIn {
		public String liveSid = "";
	}
	
	public static class LiveInfoOut extends CommandOut {
		
		public RespLiveInfo live;
		
		public LiveInfoOut() {
			super(Command.LIVE_INFO);
		}
	}
	
	private String respFail(String message) {
		String resp = CommandOut.failureOut(Command.LIVE_INFO, message).httpResp();
		return resp;
	}
	
}
