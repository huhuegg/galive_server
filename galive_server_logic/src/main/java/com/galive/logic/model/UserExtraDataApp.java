package com.galive.logic.model;

import org.mongodb.morphia.annotations.Embedded;

import com.galive.logic.model.User.UserPlatform;


@Embedded
public class UserExtraDataApp extends UserExtraData {

	private String username = "";

	private String password = "";
	
	private String profile = "";
	
	public UserExtraDataApp() {
		super();
		setPlatform(UserPlatform.App);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}
}
