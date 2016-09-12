package com.galive.logic.model;

import java.util.ArrayList;
import java.util.List;

import org.mongodb.morphia.annotations.Entity;

@Entity(value="meeting")
public class Meeting extends BaseModel {

	private String room;
	
	private String name;
	
	private MeetingMember owner;
	
	private List<MeetingMember> members = new ArrayList<>();
	
	private MeetingSetting setting;

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MeetingMember getOwner() {
		return owner;
	}

	public void setOwner(MeetingMember owner) {
		this.owner = owner;
	}

	public List<MeetingMember> getMembers() {
		return members;
	}

	public void setMembers(List<MeetingMember> members) {
		this.members = members;
	}

	public MeetingSetting getSetting() {
		return setting;
	}

	public void setSetting(MeetingSetting setting) {
		this.setting = setting;
	}
	
	
}
