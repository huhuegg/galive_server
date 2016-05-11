package com.galive.logic.model;

import org.mongodb.morphia.annotations.Embedded;


@Embedded
public class UserExtraDataWeChat extends UserExtraData {

	/**
	 * 普通用户的标识，对当前开发者帐号唯一
	 */
	private String openid = "";
	
	/**
	 * 用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。
	 */
	private String unionid = "";

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	
}
