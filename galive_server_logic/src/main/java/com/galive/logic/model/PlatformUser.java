package com.galive.logic.model;

import org.mongodb.morphia.annotations.Entity;

@Entity(value="platform_user", noClassnameStored = true)
public abstract class PlatformUser extends BaseModel {
	
	public static enum UserPlatform {
		WeChat,
		QQ,
		SinaWeibo;
		
		public static UserPlatform convert(int code) {
			for (UserPlatform p : UserPlatform.values()) {
				if (p.ordinal() == code) {
					return p;
				}
			}
			return UserPlatform.WeChat;
		}
	}
	
	protected String deviceid;
	
	protected UserPlatform platform;
	
	protected String avatar;
	
	protected String nickname;

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

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}
	
	
}
