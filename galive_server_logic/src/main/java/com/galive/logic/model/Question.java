package com.galive.logic.model;

import java.util.ArrayList;
import java.util.List;
import org.mongodb.morphia.annotations.Entity;

@Entity(value="question", noClassnameStored = true)
public class Question extends BaseModel {
	
	public static enum QuestionState {
		Pending,
		Answered;
	}
	
	private String userSid = "";

	private QuestionState state = QuestionState.Pending;

	private String desc = "";

	private List<String> imageUrls = new ArrayList<>();
	
	private String recordUrl = "";
	
	private List<String> tags = new ArrayList<>();
	
	private List<String> answers = new ArrayList<>();
	
	public String desc() {
		return String.format(" %s(%s) by %s ", desc, sid, userSid);
	}

	/* ======================= Getter Setter ======================= */
	
	public String getUserSid() {
		return userSid;
	}

	public void setUserSid(String userSid) {
		this.userSid = userSid;
	}

	public QuestionState getState() {
		return state;
	}

	public void setState(QuestionState state) {
		this.state = state;
	}

	public List<String> getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(List<String> imageUrls) {
		this.imageUrls = imageUrls;
	}

	public String getRecordUrl() {
		return recordUrl;
	}

	public void setRecordUrl(String recordUrl) {
		this.recordUrl = recordUrl;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public List<String> getAnswers() {
		return answers;
	}

	public void setAnswers(List<String> answers) {
		this.answers = answers;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
