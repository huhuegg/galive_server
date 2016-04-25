package com.galive.logic.network.socket.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.exception.LogicException;
import com.galive.logic.helper.LoggerHelper;
import com.galive.logic.model.Answer.AnswerResult;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.AnswerService;
import com.galive.logic.service.AnswerServiceImpl;
import com.galive.logic.service.LoggerService;
import com.galive.logic.service.LoggerServiceImpl;

@SocketRequestHandler(desc = "创建解答", command = Command.ANSWER_CREATE)
public class AnswerCreateHandler extends SocketBaseHandler  {

	private static Logger logger = LoggerFactory.getLogger(AnswerCreateHandler.class);

	private AnswerService answerService = new AnswerServiceImpl();
	private LoggerService loggerService = new LoggerServiceImpl();
	
	@Override
	public String handle(String userSid, String reqData) {
		try {
			LoggerHelper.appendLog("--创建解答--", logBuffer);
			AnswerCreateIn in = JSON.parseObject(reqData, AnswerCreateIn.class);
			answerService.createAnswer(in.questionSid, in.solverSid, in.result);
			
			
			CommandOut out = new CommandOut(Command.ANSWER_CREATE);
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
			LoggerHelper.appendLog("响应客户端:" + resp, logBuffer);
			LoggerHelper.appendSplit(logBuffer);
			String logicLog = LoggerHelper.loggerString(logBuffer);
			logger.error(logicLog);
			loggerService.saveLogicLog(logicLog);
			return resp;
		}
		
	}
	
	public static class AnswerCreateIn {
		public String questionSid;
		public String solverSid;
		public AnswerResult result;
	}
	
	private String respFail(String message) {
		String resp = CommandOut.failureOut(Command.ANSWER_CREATE, message).httpResp();
		return resp;
	}
	
}
