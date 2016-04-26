package com.galive.logic.network.socket.handler;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Answer;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.AnswerService;
import com.galive.logic.service.AnswerServiceImpl;
import com.galive.logic.service.QuestionService;
import com.galive.logic.service.QuestionServiceImpl;

@SocketRequestHandler(desc = "修改解答结果", command = Command.ANSWER_RESOLVE)
public class AnswerResolveHandler extends SocketBaseHandler  {

	private AnswerService answerService = new AnswerServiceImpl();
	private QuestionService questionService = new QuestionServiceImpl();
	
	@Override
	public String handle(String userSid, String reqData) throws Exception {
		appendLog("--AnswerResolveHandler(修改解答结果)--");
		AnswerResolveIn in = JSON.parseObject(reqData, AnswerResolveIn.class);
		String answerSid = in.answerSid;
		appendLog("answerSid(解答id):" + answerSid);
		Answer a = answerService.resolveAnswer(answerSid);
		
		questionService.resolveQuestion(a.getQuestionSid());
		
		CommandOut out = new CommandOut(Command.ANSWER_RESOLVE);
		String resp = out.socketResp();
		return resp;
	}
	
	public static class AnswerResolveIn {
		public String answerSid;
	}
	
}
