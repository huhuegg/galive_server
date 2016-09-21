package com.galive.logic.model.account;

import org.mongodb.morphia.annotations.Transient;

import com.galive.logic.model.BaseModel;
import com.galive.logic.model.MeetingMemberOptions;
import com.galive.logic.model.MeetingOptions;

public class Account extends BaseModel {

	private String latestLoginPlatform;
	
	private MeetingOptions meetingOptions;
	
	private MeetingMemberOptions meetingMemberOptions;
	
	@Transient
	private String nickname;
	
	@Transient
	private String avatar;
	
	@Transient
	private String wechatUnionid;
	
	
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

	public String getWechatUnionid() {
		return wechatUnionid;
	}

	public void setWechatUnionid(String wechatUnionid) {
		this.wechatUnionid = wechatUnionid;
	}
}
