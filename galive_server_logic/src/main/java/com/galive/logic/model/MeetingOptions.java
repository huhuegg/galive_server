package com.galive.logic.model;

import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class MeetingOptions {
	
	
	/**
	 * 会议名称
	 */
	private String name;
	
	/**
	 * 进入会议需要的密码
	 */
	private String password;
	
	/**
	 * 主持人视频开关
	 */
	private boolean holderVideoOn = false;
	
	/**
	 * 会议成员视频开关
	 */
	private boolean memberVideoOn = false;
	
	public static String randomName() {
		return DigestUtils.md5Hex(UUID.randomUUID().toString());
	}
	
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
