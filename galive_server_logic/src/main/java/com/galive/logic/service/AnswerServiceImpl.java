package com.galive.logic.service;

import java.util.List;

import com.galive.logic.dao.AnswerDao;
import com.galive.logic.dao.AnswerDaoImpl;
import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Answer;
import com.galive.logic.model.Answer.AnswerResult;


public class AnswerServiceImpl extends BaseService implements AnswerService {

	private AnswerDao answerDao = new AnswerDaoImpl();
	
	public AnswerServiceImpl() {
		super();
		appendLog("AnswerServiceImpl");
	}

	@Override
	public long countAnswer(String userSid, AnswerResult result) throws LogicException {
		long count = answerDao.count(userSid, result);
		appendLog("解答总数:" + count);
		return count;
	}

	@Override
	public Answer createAnswer(String questionSid, String solverSid, AnswerResult result) throws LogicException {
		Answer answer = new Answer();
		answer.setQuestionSid(questionSid);
		answer.setUserSid(solverSid);
		answer.setResult(result);
		answer.setTime(System.currentTimeMillis());
		answerDao.saveOrUpdate(answer);
		return answer;
	}

	@Override
	public Answer resolveAnswer(String answerSids) throws LogicException {
		Answer a = answerDao.find(answerSids);
		if (a == null) {
			appendLog("解答不存在。");
			throw new LogicException("解答不存在。");
		}
		a.setResult(AnswerResult.Resolved);
		a = answerDao.saveOrUpdate(a);
		return a;
	}

	@Override
	public List<Answer> listAnserByQuestion(String questionSid, int index, int size) throws LogicException {
		List<Answer> answers = null;
		if (size < 0) {
			answers = answerDao.listAllByQuestion(questionSid);
		} else {
			answers = answerDao.listByQuestion(questionSid, index, index + size - 1);
		}
		appendLog("查询解答数:" + answers.size());
		return answers;
	}

}
