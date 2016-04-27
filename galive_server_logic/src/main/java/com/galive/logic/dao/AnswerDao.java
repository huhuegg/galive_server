package com.galive.logic.dao;

import java.util.List;

import com.galive.logic.model.Answer;
import com.galive.logic.model.Answer.AnswerResult;

public interface AnswerDao {

	public Answer saveOrUpdate(Answer a);
	
	public Answer find(String answerSid);
	
	public long count(String userSid, AnswerResult result);
	
	public List<Answer> listByQuestion(String questionSid, int start, int end);
	
	public List<Answer> listAllByQuestion(String questionSid);
}
