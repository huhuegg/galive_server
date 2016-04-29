package com.galive.logic.network.socket.handler;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Answer.AnswerResult;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.AnswerService;
import com.galive.logic.service.AnswerServiceImpl;

@SocketRequestHandler(desc = "创建解答", command = Command.ANSWER_CREATE)
public class AnswerCreateHandler extends SocketBaseHandler  {

	private AnswerService answerService = new AnswerServiceImpl();
	
	@Override
	public CommandOut handle(String userSid, String reqData) throws Exception {
		appendLog("--AnswerCreateHandler(创建解答)--");
		AnswerCreateIn in = JSON.parseObject(reqData, AnswerCreateIn.class);
		String questionSid = in.questionSid;
		String solverSid = in.solverSid;
		AnswerResult result = in.result; 
		appendLog("问题id(questionSid):" + questionSid);
		appendLog("解答者id(solverSid):" + solverSid);
		appendLog("解答结果(result):" + result);
		
		answerService.createAnswer(questionSid, solverSid, result);
		
		
		CommandOut out = new CommandOut(Command.ANSWER_CREATE);
		return out;
	}
	
	public static class AnswerCreateIn {
		public String questionSid;
		public String solverSid;
		public AnswerResult result;
	}
	
	
}
