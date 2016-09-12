package com.galive.logic.model;

import org.mongodb.morphia.annotations.Entity;


@Entity(value="platform_account")
public abstract class PlatformAccount extends BaseModel {
	
	private Platform platform;
	
	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

}
