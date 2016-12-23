package com.galive.logic.model;

import java.util.ArrayList;
import java.util.List;

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
	private List<String> members = new ArrayList<>();

	public String getOwnerSid() {
		return ownerSid;
	}

	public void setOwnerSid(String ownerSid) {
		this.ownerSid = ownerSid;
	}

	public List<String> getMembers() {
		return members;
	}

	public void setMembers(List<String> members) {
		this.members = members;
	}
}
