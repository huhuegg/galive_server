package com.galive.logic.dao;

import java.util.List;

import com.galive.logic.model.Question;

public interface QuestionDao {

	public Question saveOrUpdate(Question q);
	
	public Question find(String sid);
	
	public List<Question> listByCreateTime(int start, int end);
	
	public long count(String userSid);
	
}
