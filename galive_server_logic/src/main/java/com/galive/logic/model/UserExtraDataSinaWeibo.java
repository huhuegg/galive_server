package com.galive.logic.model;

import org.mongodb.morphia.annotations.Embedded;


@Embedded
public class UserExtraDataSinaWeibo extends UserExtraData {

	/**
	 * 普通用户的标识，对当前开发者帐号唯一
	 */
	private String userID = "";

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	
	
}
