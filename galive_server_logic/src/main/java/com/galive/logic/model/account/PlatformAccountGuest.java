 package com.galive.logic.model.account;

import org.mongodb.morphia.annotations.Entity;


@Entity(value="platform_account")
public class PlatformAccountGuest extends PlatformAccount {
	
	private String name = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
