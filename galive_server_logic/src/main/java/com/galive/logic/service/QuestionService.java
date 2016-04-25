package com.galive.logic.service;

import java.util.List;

import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Question;

public interface QuestionService {

	public Question create(String desc, List<String> imageUrls, String recordUrl, List<String> tags) throws LogicException;
	
	public List<Question> listByCreateTime(int index, int size) throws LogicException;
	
	public Question findBySid(String questionSid) throws LogicException;
	
	public long countQuestion(String userSid) throws LogicException;
}
