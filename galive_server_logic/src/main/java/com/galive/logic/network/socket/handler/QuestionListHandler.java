package com.galive.logic.network.socket.handler;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.PageCommandOut;
import com.galive.common.protocol.PageParams;
import com.galive.logic.model.Question;
import com.galive.logic.network.model.RespQuestion;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.QuestionService;
import com.galive.logic.service.QuestionServiceImpl;

@SocketRequestHandler(desc = "问题列表", command = Command.QUESTION_LIST)
public class QuestionListHandler extends SocketBaseHandler  {

	public static enum QuestionListBy {
		CreateTime;
		
		public static QuestionListBy convert(int i) {
			for (QuestionListBy by : QuestionListBy.values()) {
				if (by.ordinal() == i) {
					return by;
				}
			}
			return QuestionListBy.CreateTime;
		}
	}
	private QuestionService questionService = new QuestionServiceImpl();
	
	@Override
	public String handle(String userSid, String reqData) throws Exception {
		appendLog("--QuestionListHandler(问题列表)--");
		QuestionListIn in = JSON.parseObject(reqData, QuestionListIn.class);
		
		int listBy = in.listBy;
		int index = in.index;
		int size = in.size; 
		appendLog("排序方式(listBy):" + listBy);
		appendLog("起始游标(index):" + index);
		appendLog("分页数量(size):" + size);

		QuestionListBy by = QuestionListBy.convert(listBy);
		List<Question> questions = new ArrayList<>();
		if (by == QuestionListBy.CreateTime) {
			questions = questionService.listQuestionByCreateTime(index, size);
		}
		
		PageCommandOut<RespQuestion> out = new PageCommandOut<>(Command.QUESTION_LIST, in);
		List<RespQuestion> rqs = new ArrayList<>();
		for (Question q : questions) {
			RespQuestion rq = new RespQuestion();
			rq.convert(q);
			rqs.add(rq);
		}
		out.setData(rqs);
		String resp = out.socketResp();
		return resp;	
	}
	
	public static class QuestionListIn extends PageParams {
		public int listBy;
	}
}
