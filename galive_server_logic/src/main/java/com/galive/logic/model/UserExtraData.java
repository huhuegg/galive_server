package com.galive.logic.model;

import org.mongodb.morphia.annotations.Embedded;

import com.galive.logic.model.User.UserPlatform;



@Embedded
public abstract class UserExtraData {

	private UserPlatform platform = UserPlatform.App;

	public UserPlatform getPlatform() {
		return platform;
	}

	public void setPlatform(UserPlatform platform) {
		this.platform = platform;
	}

	
	
}
