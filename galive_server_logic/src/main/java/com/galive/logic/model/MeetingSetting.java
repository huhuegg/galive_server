package com.galive.logic.model;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class MeetingSetting {
	
	private String password;
	
	private boolean lockOnStart = false;
	
	private boolean onlyHostShare = false;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isLockOnStart() {
		return lockOnStart;
	}

	public void setLockOnStart(boolean lockOnStart) {
		this.lockOnStart = lockOnStart;
	}

	public boolean isOnlyHostShare() {
		return onlyHostShare;
	}

	public void setOnlyHostShare(boolean onlyHostShare) {
		this.onlyHostShare = onlyHostShare;
	}
	
	
}
