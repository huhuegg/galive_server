package com.galive.logic.model;

import java.util.HashSet;
import java.util.Set;

/**
 * 房间
 * @author luguangqing
 *
 */
public class Room extends BaseModel {
	
	/**
	 * 房间主持人
	 */
	private String ownerSid = "";
	
	/**
	 * 房间内成员
	 */
	private Set<String> members = new HashSet<>();
	
	private boolean screenShared = false;

	public String getOwnerSid() {
		return ownerSid;
	}

	public void setOwnerSid(String ownerSid) {
		this.ownerSid = ownerSid;
	}

	public boolean isScreenShared() {
		return screenShared;
	}

	public void setScreenShared(boolean screenShared) {
		this.screenShared = screenShared;
	}

	public Set<String> getMembers() {
		return members;
	}

	public void setMembers(Set<String> members) {
		this.members = members;
	}
}
