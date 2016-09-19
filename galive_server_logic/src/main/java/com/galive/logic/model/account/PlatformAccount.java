package com.galive.logic.model.account;

import org.mongodb.morphia.annotations.Entity;

import com.galive.logic.model.BaseModel;

@Entity(value="platform_account")
public abstract class PlatformAccount extends BaseModel {
	
	private String accountSid;
	
	private Platform platform;
	
	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public String getAccountSid() {
		return accountSid;
	}

	public void setAccountSid(String accountSid) {
		this.accountSid = accountSid;
	}

}
