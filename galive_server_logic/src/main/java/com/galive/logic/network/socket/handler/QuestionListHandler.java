package com.galive.logic.network.socket.handler;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.common.protocol.PageCommandOut;
import com.galive.common.protocol.PageParams;
import com.galive.logic.exception.LogicException;
import com.galive.logic.helper.LoggerHelper;
import com.galive.logic.model.Question;
import com.galive.logic.network.model.RespQuestion;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.LoggerService;
import com.galive.logic.service.LoggerServiceImpl;
import com.galive.logic.service.QuestionService;
import com.galive.logic.service.QuestionServiceImpl;

@SocketRequestHandler(desc = "问题列表", command = Command.QUESTION_LIST)
public class QuestionListHandler extends SocketBaseHandler  {

	public static enum QuestionListBy {
		CreateTime
	}
	
	private static Logger logger = LoggerFactory.getLogger(QuestionListHandler.class);

	private QuestionService questionService = new QuestionServiceImpl();
	private LoggerService loggerService = new LoggerServiceImpl();
	
	@Override
	public String handle(String userSid, String reqData) {
		try {
			LoggerHelper.appendLog("--问题列表--", logBuffer);
			QuestionListIn in = JSON.parseObject(reqData, QuestionListIn.class);
			List<Question> questions = questionService.listByCreateTime(in.index, in.size);
			
			
			PageCommandOut<RespQuestion> out = new PageCommandOut<>(Command.QUESTION_LIST, in);
			List<RespQuestion> rqs = new ArrayList<>();
			for (Question q : questions) {
				RespQuestion rq = new RespQuestion();
				rq.convert(q);
				rqs.add(rq);
			}
			out.setData(rqs);
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
	
	public static class QuestionListIn extends PageParams {
		public QuestionListBy listBy;
	}
	
	private String respFail(String message) {
		String resp = CommandOut.failureOut(Command.QUESTION_LIST, message).httpResp();
		return resp;
	}
	
}
