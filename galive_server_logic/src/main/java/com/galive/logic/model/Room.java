package com.galive.logic.model;

import java.util.HashSet;
import java.util.Set;

public class Room {
	
	public static enum RoomPrivacy {
		Public,
		Privacy
	}

	private String sid = "";
	
	private String ownerId = "";
	
	private RoomPrivacy privacy;
	
	private String name = "";
	
	private int maxMemberCount = 0;
	
	private Set<String> users = new HashSet<>();
	
	/**
	 * 邀请人,仅当Privacy时候有
	 */
	private Set<String> invitees = new HashSet<>();
	
	public String desc() {
		return " " + name + "( " + sid + ") "; 
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<String> getUsers() {
		return users;
	}

	public void setUsers(Set<String> users) {
		this.users = users;
	}

	public int getMaxMemberCount() {
		return maxMemberCount;
	}

	public void setMaxMemberCount(int maxMemberCount) {
		this.maxMemberCount = maxMemberCount;
	}

	public RoomPrivacy getPrivacy() {
		return privacy;
	}

	public void setPrivacy(RoomPrivacy privacy) {
		this.privacy = privacy;
	}

	public Set<String> getInvitees() {
		return invitees;
	}

	public void setInvitees(Set<String> invitees) {
		this.invitees = invitees;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}


	
}
