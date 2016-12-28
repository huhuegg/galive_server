package com.galive.logic.model.account;

import org.mongodb.morphia.annotations.Entity;

import com.galive.logic.model.BaseModel;

@Entity(value="platform_account")
public class PlatformAccount extends BaseModel {
	
	/**
	 * 绑定的accountSid
	 */
	private String accountSid;
	
	/**
	 * 用户平台
	 */
	private Platform platform;
	
	/**
	 * 平台用户唯一标识。
	 */
	private String platformUnionid = "";
	
	
	public String getAccountSid() {
		return accountSid;
	}
	public void setAccountSid(String accountSid) {
		this.accountSid = accountSid;
	}
	public Platform getPlatform() {
		return platform;
	}
	public void setPlatform(Platform platform) {
		this.platform = platform;
	}
	public String getPlatformUnionid() {
		return platformUnionid;
	}
	public void setPlatformUnionid(String platformUnionid) {
		this.platformUnionid = platformUnionid;
	}
	
}
