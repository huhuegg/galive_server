package com.galive.logic.model;

import org.mongodb.morphia.annotations.Embedded;

import com.galive.logic.model.User.UserGender;
import com.galive.logic.model.User.UserPlatform;



@Embedded
public class UserExtraData {

	private UserPlatform platform = UserPlatform.App;

	private String nickname = "";

	private String avatar = "";
	
	private UserGender gender = UserGender.Unknown;
	
	public UserPlatform getPlatform() {
		return platform;
	}

	public void setPlatform(UserPlatform platform) {
		this.platform = platform;
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

	public UserGender getGender() {
		return gender;
	}

	public void setGender(UserGender gender) {
		this.gender = gender;
	}
	
}
