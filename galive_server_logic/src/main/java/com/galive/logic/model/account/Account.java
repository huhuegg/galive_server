package com.galive.logic.model.account;

import com.galive.logic.model.BaseModel;
import com.galive.logic.model.MeetingMemberOptions;
import com.galive.logic.model.MeetingOptions;

public class Account extends BaseModel {

	private String latestLoginPlatform;
	
	private MeetingOptions meetingOptions;
	
	private MeetingMemberOptions meetingMemberOptions;
	
	public static Account createNewAccount() {
		Account act = new Account();
		
		return act;
	}

	public MeetingOptions getMeetingOptions() {
		return meetingOptions;
	}

	public void setMeetingOptions(MeetingOptions meetingOptions) {
		this.meetingOptions = meetingOptions;
	}

	public MeetingMemberOptions getMeetingMemberOptions() {
		return meetingMemberOptions;
	}

	public void setMeetingMemberOptions(MeetingMemberOptions meetingMemberOptions) {
		this.meetingMemberOptions = meetingMemberOptions;
	}

	public String getLatestLoginPlatform() {
		return latestLoginPlatform;
	}

	public void setLatestLoginPlatform(String latestLoginPlatform) {
		this.latestLoginPlatform = latestLoginPlatform;
	}
}
