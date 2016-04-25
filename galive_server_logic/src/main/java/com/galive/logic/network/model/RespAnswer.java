package com.galive.logic.network.model;

import com.galive.logic.model.Answer;

public class RespAnswer {

	public String userSid = "";
	
	public String questionSid = "";
	
	public int result;

	public long time = 0l;
	
	public void convert(Answer a) {
		userSid = a.getUserSid();
		questionSid = a.getQuestionSid();
		result = a.getResult().ordinal();
		time = a.getTime();
	}
	
}
