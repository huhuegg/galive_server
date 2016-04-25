package com.galive.logic.network.socket.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.exception.LogicException;
import com.galive.logic.helper.LoggerHelper;
import com.galive.logic.model.Answer;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.AnswerService;
import com.galive.logic.service.AnswerServiceImpl;
import com.galive.logic.service.LoggerService;
import com.galive.logic.service.LoggerServiceImpl;
import com.galive.logic.service.QuestionService;
import com.galive.logic.service.QuestionServiceImpl;

@SocketRequestHandler(desc = "修改解答结果", command = Command.ANSWER_RESOLVE)
public class AnswerResolveHandler extends SocketBaseHandler  {

	private static Logger logger = LoggerFactory.getLogger(AnswerResolveHandler.class);

	private AnswerService answerService = new AnswerServiceImpl();
	private QuestionService questionService = new QuestionServiceImpl();
	private LoggerService loggerService = new LoggerServiceImpl();
	
	@Override
	public String handle(String userSid, String reqData) {
		try {
			LoggerHelper.appendLog("--修改解答结果--", logBuffer);
			AnswerResolveIn in = JSON.parseObject(reqData, AnswerResolveIn.class);
			Answer a = answerService.resolveAnswer(in.answerSid);
			
			questionService.resolveQuestion(a.getQuestionSid());
			
			CommandOut out = new CommandOut(Command.ANSWER_RESOLVE);
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
	
	public static class AnswerResolveIn {
		public String answerSid;
	}
	
	private String respFail(String message) {
		String resp = CommandOut.failureOut(Command.ANSWER_RESOLVE, message).httpResp();
		return resp;
	}
	
}
