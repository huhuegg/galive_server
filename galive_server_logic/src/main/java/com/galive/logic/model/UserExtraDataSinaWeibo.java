package com.galive.logic.model;

import org.mongodb.morphia.annotations.Embedded;

import com.galive.logic.model.User.UserPlatform;


@Embedded
public class UserExtraDataSinaWeibo extends UserExtraData {

	/**
	 * 普通用户的标识，对当前开发者帐号唯一
	 */
	private String userID = "";

	public UserExtraDataSinaWeibo() {
		super();
		setPlatform(UserPlatform.SinaWeibo);
	}
	
	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	
	
}
