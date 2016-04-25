package com.galive.logic.dao;

import com.galive.logic.model.Answer;

public interface AnswerDao {

	public Answer saveOrUpdate(Answer a);
	
	public Answer find(String answerSid);
	
	public long count(String userSid);
}
