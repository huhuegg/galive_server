package com.galive.logic.service;

import java.util.List;

import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Question;

public interface QuestionService {

	public Question createQuestion(String desc, List<String> imageUrls, String recordUrl, List<String> tags) throws LogicException;
	
	public void resolveQuestion(String questionSid) throws LogicException;
	
	public List<Question> listQuestionByCreateTime(int index, int size) throws LogicException;
	
	public Question findQuestionBySid(String questionSid) throws LogicException;
	
	public long countQuestion(String userSid) throws LogicException;
	
	public List<String> listQuestionTags() throws LogicException;
	
	public void addQuestionTag(String tag) throws LogicException;
	
	public void removeQuestionTag(String tag) throws LogicException;
}
