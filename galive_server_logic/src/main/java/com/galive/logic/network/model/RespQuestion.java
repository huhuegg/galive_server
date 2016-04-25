package com.galive.logic.network.model;

import java.util.ArrayList;
import java.util.List;

import com.galive.logic.model.Question;

public class RespQuestion {

	public String userSid = "";

	public int state;

	public String desc = "";

	public List<String> imageUrls = new ArrayList<>();
	
	public String recordUrl = "";
	
	public List<String> tags = new ArrayList<>();
	
	public void convert(Question q) {
		userSid = q.getUserSid();
		state = q.getState().ordinal();
		desc = q.getDesc();
		imageUrls = q.getImageUrls();
		recordUrl = q.getRecordUrl();
		tags = q.getTags();
	}
	
}
