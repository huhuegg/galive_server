package com.galive.logic.model;

import java.util.LinkedList;
import java.util.List;

public class Live {
	
	/**
	 * 直播房间id
	 */
	private String sid = "";
	
	/**
	 * 创建人
	 */
	private String account = "";
	
	/**
	 * 房间内成员
	 */
	private List<String> accounts = new LinkedList<String>();
	
	/**
	 * 创建时间
	 */
	private long createAt = System.currentTimeMillis();
	
	/**
	 * 房间id
	 */
	private String room = "";
	
	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}
	
	public long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(long createAt) {
		this.createAt = createAt;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public List<String> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<String> accounts) {
		this.accounts = accounts;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	
}
