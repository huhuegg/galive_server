package com.galive.logic.model;

import java.util.ArrayList;
import java.util.List;

public class Live {

	private String sid = "";
	
	private String ownerAccount = "";
	
	private List<String> memberAccounts = new ArrayList<>();
	
	public String getOwnerAccount() {
		return ownerAccount;
	}

	public void setOwnerAccount(String ownerAccount) {
		this.ownerAccount = ownerAccount;
	}

	public List<String> getMemberAccounts() {
		return memberAccounts;
	}

	public void setMemberAccounts(List<String> memberAccounts) {
		this.memberAccounts = memberAccounts;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}
}
