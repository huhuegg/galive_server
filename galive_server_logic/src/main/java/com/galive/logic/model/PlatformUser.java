package com.galive.logic.model;

import org.mongodb.morphia.annotations.Entity;

@Entity(value="platform_user", noClassnameStored = true)
public abstract class PlatformUser extends BaseModel {
	
	public static enum UserPlatform {
		WeChat,
		QQ,
		SinaWeibo
	}

	protected String udid;
	
	protected UserPlatform platform;
	
	protected String avatar;
	
	protected String nickname;

	public String getUdid() {
		return udid;
	}

	public void setUdid(String udid) {
		this.udid = udid;
	}

	public UserPlatform getPlatform() {
		return platform;
	}

	public void setPlatform(UserPlatform platform) {
		this.platform = platform;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	
}
