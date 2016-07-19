package com.galive.logic.model;

public class Live {

	public static enum LiveState {
		On,
		Off;
	}
	
	private String sid = "";
	
	private String name = "";
	
	private String ownerSid = "";
	
	private long createAt = System.currentTimeMillis();
	
	private long latestLiveAt = System.currentTimeMillis();
	
	private LiveState state = LiveState.Off;
	
	private String actionRecordUrl = "";
	
	public String desc() {
		return " " + name + "(" + sid + ") ";
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwnerSid() {
		return ownerSid;
	}

	public void setOwnerSid(String ownerSid) {
		this.ownerSid = ownerSid;
	}

	public long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(long createAt) {
		this.createAt = createAt;
	}

	public long getLatestLiveAt() {
		return latestLiveAt;
	}

	public void setLatestLiveAt(long latestLiveAt) {
		this.latestLiveAt = latestLiveAt;
	}

	public LiveState getState() {
		return state;
	}

	public void setState(LiveState state) {
		this.state = state;
	}

	public String getActionRecordUrl() {
		return actionRecordUrl;
	}

	public void setActionRecordUrl(String actionRecordUrl) {
		this.actionRecordUrl = actionRecordUrl;
	}
	
	
}
