package com.galive.logic.model;

import org.mongodb.morphia.annotations.Embedded;

import com.galive.logic.model.User.UserPlatform;


@Embedded
public class UserExtraDataApp extends UserExtraData {

	
	
	private String profile = "";
	
	public UserExtraDataApp() {
		super();
		setPlatform(UserPlatform.App);
	}

	

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}
}
