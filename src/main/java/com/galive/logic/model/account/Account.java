package com.galive.logic.model.account;

import org.mongodb.morphia.annotations.Entity;
import com.galive.logic.model.BaseModel;

/**
 * 用户帐号
 * @author luguangqing
 *
 */
@Entity
public class Account extends BaseModel {
	
	/**
	 * 用户昵称
	 */
	private String nickname = "";
	
	/**
	 * 用户头像
	 */
	private String avatar = "";
	
	/**
	 * 用户性别
	 */
	private Gender gender = Gender.U;
	
	/**
	 * 个人简介
	 */
	private String profile = "";

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

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}	
}
