package com.galive.logic.model;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class MeetingMember {

	private String account;
	
	private String displayName;

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
}
