package com.galive.logic.model;

import org.mongodb.morphia.annotations.Embedded;

import com.galive.logic.model.User.UserPlatform;


@Embedded
public class UserExtraDataQQ extends UserExtraData {

	/**
	 * 普通用户的标识，对当前开发者帐号唯一
	 */
	private String openid = "";

	public UserExtraDataQQ() {
		super();
		setPlatform(UserPlatform.QQ);
	}
	
	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}
}
