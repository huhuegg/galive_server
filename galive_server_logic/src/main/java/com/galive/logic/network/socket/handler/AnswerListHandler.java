package com.galive.logic.network.socket.handler;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.PageCommandOut;
import com.galive.common.protocol.PageParams;
import com.galive.logic.model.Answer;
import com.galive.logic.network.model.RespAnswer;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.AnswerService;
import com.galive.logic.service.AnswerServiceImpl;

@SocketRequestHandler(desc = "解答列表", command = Command.ANSWER_LIST)
public class AnswerListHandler extends SocketBaseHandler  {
	

	private AnswerService answerService = new AnswerServiceImpl();
	
	@Override
	public String handle(String userSid, String reqData) throws Exception {
		appendLog("--AnswerListHandler(问题列表)--");
		AnswerListIn in = JSON.parseObject(reqData, AnswerListIn.class);
		
		String questionSid = in.questionSid;
		int index = in.index;
		int size = in.size; 
		appendLog("问题id(questionSid):" + questionSid);
		appendLog("起始游标(index):" + index);
		appendLog("分页数量(size):" + size);
		
		List<Answer> answers = answerService.listAnserByQuestion(questionSid, index, size);
		
		PageCommandOut<RespAnswer> out = new PageCommandOut<>(Command.ANSWER_LIST, in);
		List<RespAnswer> ras = new ArrayList<>();
		for (Answer a : answers) {
			RespAnswer ra = new RespAnswer();
			ra.convert(a);
			ras.add(ra);
		}
		out.setData(ras);
		String resp = out.socketResp();
		return resp;
		
	}
	
	public static class AnswerListIn extends PageParams {
		public String questionSid;
	}
}
