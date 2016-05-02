package com.galive.logic.network.model;

import com.galive.logic.model.Answer;

public class RespAnswer {

	public String sid = "";
	
	public RespUser resolver;
	
	public String questionSid = "";
	
	public int result;

	public long time = 0l;
	
	public void convert(Answer a) {
		sid = a.getSid();
		questionSid = a.getQuestionSid();
		result = a.getResult().ordinal();
		time = a.getTime();
	}
	
}
