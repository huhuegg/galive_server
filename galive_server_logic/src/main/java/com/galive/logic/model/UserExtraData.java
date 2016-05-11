package com.galive.logic.model;

import org.mongodb.morphia.annotations.Embedded;

import com.galive.logic.model.User.UserPlatform;



@Embedded
public abstract class UserExtraData {

	// 三方平台
	private UserPlatform platform = UserPlatform.WeChat;

	public UserPlatform getPlatform() {
		return platform;
	}

	public void setPlatform(UserPlatform platform) {
		this.platform = platform;
	}
	
}
