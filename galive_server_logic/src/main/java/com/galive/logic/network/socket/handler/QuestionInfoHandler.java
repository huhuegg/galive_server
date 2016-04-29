package com.galive.logic.network.socket.handler;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Answer;
import com.galive.logic.model.Answer.AnswerResult;
import com.galive.logic.model.Question;
import com.galive.logic.model.User;
import com.galive.logic.network.model.RespAnswer;
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
	public CommandOut handle(String userSid, String reqData) throws Exception {
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
		
		out.answerCount = answerService.countAnswer(questionOwnerSid, null);
		out.questionCount  = questionService.countQuestion(questionOwnerSid);
		out.questionSolvedCount = answerService.countAnswer(questionOwnerSid, AnswerResult.Resolved);
		
		List<Answer> answers = answerService.listAnserByQuestion(questionSid, 0, -1);
		List<RespAnswer> ras = new ArrayList<>();
		for (Answer a : answers) {
			RespAnswer ra = new RespAnswer();
			ra.convert(a);
			User resolver = userService.findUserBySid(a.getUserSid());
			RespUser ru = new RespUser();
			ru.convert(resolver);
			ra.resolver = ru;
			ras.add(ra);
		}
		out.answers = ras;
		return out;
	}
	
	public static class QuestionInfoIn {
		public String questionSid = "";
	}
	
	public static class QuestionInfoOut extends CommandOut {
		
		public RespQuestion question;
		public RespUser questioner;
		public long answerCount;
		public long questionCount;
		public long questionSolvedCount;
		public List<RespAnswer> answers = new ArrayList<>();
		
		public QuestionInfoOut() {
			super(Command.QUESTION_INFO);
		}
	}
	
}
