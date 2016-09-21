package com.galive.logic.model.account;

import org.mongodb.morphia.annotations.Transient;

import com.alibaba.fastjson.annotation.JSONField;
import com.galive.logic.model.BaseModel;
import com.galive.logic.model.MeetingMemberOptions;
import com.galive.logic.model.MeetingOptions;

public class Account extends BaseModel {

	@JSONField(serialize = false)
	private String latestLoginPlatform;
	
	@JSONField(name="meeting_opt")
	private MeetingOptions meetingOptions;
	
	@JSONField(name="meeting_member_opt")
	private MeetingMemberOptions meetingMemberOptions;
	
	@Transient
	private String nickname;
	
	@Transient
	private String avatar;
	
	@Transient
	private String platformSid;
	
	@Transient
	private Platform platform;
	
	
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

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public String getPlatformSid() {
		return platformSid;
	}

	public void setPlatformSid(String platformSid) {
		this.platformSid = platformSid;
	}
}
