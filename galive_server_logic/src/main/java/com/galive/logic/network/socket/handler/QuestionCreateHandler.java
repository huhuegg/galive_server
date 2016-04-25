package com.galive.logic.network.socket.handler;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.exception.LogicException;
import com.galive.logic.helper.LoggerHelper;
import com.galive.logic.model.Question;
import com.galive.logic.network.model.RespQuestion;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.LoggerService;
import com.galive.logic.service.LoggerServiceImpl;
import com.galive.logic.service.QuestionService;
import com.galive.logic.service.QuestionServiceImpl;

@SocketRequestHandler(desc = "创建问题", command = Command.QUESTION_CREATE)
public class QuestionCreateHandler extends SocketBaseHandler  {

	private static Logger logger = LoggerFactory.getLogger(QuestionCreateHandler.class);

	private QuestionService questionService = new QuestionServiceImpl();
	private LoggerService loggerService = new LoggerServiceImpl();
	
	@Override
	public String handle(String userSid, String reqData) {
		try {
			LoggerHelper.appendLog("--创建问题--", logBuffer);
			QuestionCreateIn in = JSON.parseObject(reqData, QuestionCreateIn.class);
			Question q = questionService.create(in.desc, in.imageUrls, in.recordUrl, in.tags);
			
			
			QuestionCreateOut out = new QuestionCreateOut();
			RespQuestion rq = new RespQuestion();
			rq.convert(q);
			out.question = rq;
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
	
	public static class QuestionCreateIn {
		public String desc = "";
		public List<String> imageUrls = new ArrayList<>();
		public String recordUrl = "";
		public List<String> tags = new ArrayList<>();
	}
	
	public static class QuestionCreateOut extends CommandOut {
		
		public RespQuestion question;
		
		public QuestionCreateOut() {
			super(Command.QUESTION_CREATE);
		}
	}
	
	private String respFail(String message) {
		String resp = CommandOut.failureOut(Command.QUESTION_CREATE, message).httpResp();
		return resp;
	}
	
}
