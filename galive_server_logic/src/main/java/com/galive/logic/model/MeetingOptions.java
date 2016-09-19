package com.galive.logic.model;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class MeetingOptions {
	
	private String password;
	
	private boolean holderVideoOn = false;
	
	private boolean memberVideoOn = false;
	
	private boolean allMuted = true;
	
	private boolean useSpeaker = true;
	
	private boolean voiceMuted = true;
	
	private boolean videoClosed = true;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isHolderVideoOn() {
		return holderVideoOn;
	}

	public void setHolderVideoOn(boolean holderVideoOn) {
		this.holderVideoOn = holderVideoOn;
	}

	public boolean isMemberVideoOn() {
		return memberVideoOn;
	}

	public void setMemberVideoOn(boolean memberVideoOn) {
		this.memberVideoOn = memberVideoOn;
	}
	
}
