package com.galive.logic.model;

import org.mongodb.morphia.annotations.Transient;

public class MeetingMember {

	private String accountSid;
	
	private MeetingMemberOptions options;
	
	@Transient
	private String nickname = "";
	
	@Transient
	private String avatar = "";

	public String getAccountSid() {
		return accountSid;
	}

	public void setAccountSid(String accountSid) {
		this.accountSid = accountSid;
	}

	public MeetingMemberOptions getOptions() {
		return options;
	}

	public void setOptions(MeetingMemberOptions options) {
		this.options = options;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	


}
