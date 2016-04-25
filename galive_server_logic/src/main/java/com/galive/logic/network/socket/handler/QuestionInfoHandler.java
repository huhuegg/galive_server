package com.galive.logic.network.socket.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.exception.LogicException;
import com.galive.logic.helper.LoggerHelper;
import com.galive.logic.model.Question;
import com.galive.logic.model.User;
import com.galive.logic.network.model.RespQuestion;
import com.galive.logic.network.model.RespUser;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.AnswerService;
import com.galive.logic.service.AnswerServiceImpl;
import com.galive.logic.service.LoggerService;
import com.galive.logic.service.LoggerServiceImpl;
import com.galive.logic.service.QuestionService;
import com.galive.logic.service.QuestionServiceImpl;
import com.galive.logic.service.UserService;
import com.galive.logic.service.UserServiceImpl;

@SocketRequestHandler(desc = "问题详情", command = Command.QUESTION_INFO)
public class QuestionInfoHandler extends SocketBaseHandler  {

	private static Logger logger = LoggerFactory.getLogger(QuestionInfoHandler.class);

	private QuestionService questionService = new QuestionServiceImpl();
	private AnswerService answerService = new AnswerServiceImpl();
	private UserService userService = new UserServiceImpl(logBuffer);
	private LoggerService loggerService = new LoggerServiceImpl();
	
	@Override
	public String handle(String userSid, String reqData) {
		try {
			LoggerHelper.appendLog("--问题详情--", logBuffer);
			QuestionInfoIn in = JSON.parseObject(reqData, QuestionInfoIn.class);
			Question q = questionService.findQuestionBySid(in.questionSid);
			
			QuestionInfoOut out = new QuestionInfoOut();
			RespQuestion rq = new RespQuestion();
			rq.convert(q);
			out.question = rq;
			
			String questionOwnerSid = q.getUserSid();
			RespUser questioner = new RespUser();
			User u = userService.findUserBySid(questionOwnerSid);
			questioner.convert(u);
			out.questioner = questioner;
			
			out.answerCount = answerService.countAnswer(questionOwnerSid);
			out.questionCount  = questionService.countQuestion(questionOwnerSid);
			
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
	
	public static class QuestionInfoIn {
		public String questionSid = "";
	}
	
	public static class QuestionInfoOut extends CommandOut {
		
		public RespQuestion question;
		public RespUser questioner;
		public long answerCount;
		public long questionCount;
		
		public QuestionInfoOut() {
			super(Command.QUESTION_INFO);
		}
	}
	
	private String respFail(String message) {
		String resp = CommandOut.failureOut(Command.QUESTION_INFO, message).httpResp();
		return resp;
	}
	
}
