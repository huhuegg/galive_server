package com.galive.logic.network.socket.handler;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Question;
import com.galive.logic.model.User;
import com.galive.logic.network.model.RespQuestion;
import com.galive.logic.network.model.RespUser;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.AnswerService;
import com.galive.logic.service.AnswerServiceImpl;
import com.galive.logic.service.QuestionService;
import com.galive.logic.service.QuestionServiceImpl;
import com.galive.logic.service.UserService;
import com.galive.logic.service.UserServiceImpl;

@SocketRequestHandler(desc = "问题详情", command = Command.QUESTION_INFO)
public class QuestionInfoHandler extends SocketBaseHandler  {
	
	private QuestionService questionService = new QuestionServiceImpl();
	private AnswerService answerService = new AnswerServiceImpl();
	private UserService userService = new UserServiceImpl();
	
	@Override
	public String handle(String userSid, String reqData) throws Exception {
		appendLog("--QuestionInfoHandler(问题详情)--");
		QuestionInfoIn in = JSON.parseObject(reqData, QuestionInfoIn.class);
		
		String questionSid = in.questionSid;
		appendLog("问题id(questionSid):" + questionSid);
		
		Question q = questionService.findQuestionBySid(questionSid);
		
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
		return resp;
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
	
}
