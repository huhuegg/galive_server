package com.galive.logic.service;

import java.util.List;

import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Answer;
import com.galive.logic.model.Answer.AnswerResult;

public interface AnswerService {

	public Answer createAnswer(String questionSid, String solverSid, AnswerResult result) throws LogicException;
	
	public long countAnswer(String userSid) throws LogicException;
	
	public Answer resolveAnswer(String answerSid) throws LogicException;
	
	public List<Answer> listAnserByQuestion(String questionSid, int index, int size) throws LogicException;
}
