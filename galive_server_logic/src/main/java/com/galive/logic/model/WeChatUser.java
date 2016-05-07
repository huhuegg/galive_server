package com.galive.logic.model;

import org.mongodb.morphia.annotations.Entity;

@Entity(value="platform_user")
public class WeChatUser extends PlatformUser {
	
	private String unionid;

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	

}
