package com.galive.logic.model;

import org.mongodb.morphia.annotations.Entity;

@Entity(value="answer", noClassnameStored = true)
public class Answer extends BaseModel {
	
	public static enum AnswerResult {
		Unresolved,
		Resolved;
	}
	
	private String userSid = "";
	
	private String questionSid = "";
	
	private AnswerResult result = AnswerResult.Unresolved;

	private long time = 0l;

	
	/* ======================= Getter Setter ======================= */
	public String getUserSid() {
		return userSid;
	}

	public void setUserSid(String userSid) {
		this.userSid = userSid;
	}

	public String getQuestionSid() {
		return questionSid;
	}

	public void setQuestionSid(String questionSid) {
		this.questionSid = questionSid;
	}

	public AnswerResult getResult() {
		return result;
	}

	public void setResult(AnswerResult result) {
		this.result = result;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	
	
	
	
	
}

