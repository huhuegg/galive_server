package com.galive.logic.config;

import java.util.ArrayList;
import java.util.List;

public class LogicConfig {

	private short roomMaxUser = 2;
	
	private List<String> defaultQuestionTags = new ArrayList<>();

	public short getRoomMaxUser() {
		return roomMaxUser;
	}

	public void setRoomMaxUser(short roomMaxUser) {
		this.roomMaxUser = roomMaxUser;
	}

	public List<String> getDefaultQuestionTags() {
		return defaultQuestionTags;
	}

	public void setDefaultQuestionTags(List<String> defaultQuestionTags) {
		this.defaultQuestionTags = defaultQuestionTags;
	}

	
	
}
