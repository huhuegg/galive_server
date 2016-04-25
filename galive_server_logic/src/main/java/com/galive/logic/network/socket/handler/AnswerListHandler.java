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
import com.galive.logic.model.Answer;
import com.galive.logic.network.model.RespAnswer;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.AnswerService;
import com.galive.logic.service.AnswerServiceImpl;
import com.galive.logic.service.LoggerService;
import com.galive.logic.service.LoggerServiceImpl;

@SocketRequestHandler(desc = "解答列表", command = Command.ANSWER_LIST)
public class AnswerListHandler extends SocketBaseHandler  {
	
	private static Logger logger = LoggerFactory.getLogger(AnswerListHandler.class);

	private AnswerService answerService = new AnswerServiceImpl();
	private LoggerService loggerService = new LoggerServiceImpl();
	
	@Override
	public String handle(String userSid, String reqData) {
		try {
			LoggerHelper.appendLog("--解答列表--", logBuffer);
			AnswerListIn in = JSON.parseObject(reqData, AnswerListIn.class);
			List<Answer> answers = answerService.listAnserByQuestion(in.questionSid, in.index, in.size);
			
			
			PageCommandOut<RespAnswer> out = new PageCommandOut<>(Command.ANSWER_LIST, in);
			List<RespAnswer> ras = new ArrayList<>();
			for (Answer a : answers) {
				RespAnswer ra = new RespAnswer();
				ra.convert(a);
				ras.add(ra);
			}
			out.setData(ras);
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
	
	public static class AnswerListIn extends PageParams {
		public String questionSid;
	}
	
	private String respFail(String message) {
		String resp = CommandOut.failureOut(Command.ANSWER_LIST, message).httpResp();
		return resp;
	}
	
}
