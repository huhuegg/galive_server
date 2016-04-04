package com.galive.logic.network.model;

import java.util.ArrayList;
import java.util.List;
import com.galive.logic.model.Room;

public class RespRoom {

	public String name = "";
	
	public String sid = "";
	
	public String ownerId = "";
	
	public int maxUser = 2;
	
	public List<RespUser> users = new ArrayList<>();
	
	public static RespRoom convert(Room room) {
		RespRoom rr = new RespRoom();
		rr.sid = room.getSid();
		rr.ownerId = room.getOwnerId();
		rr.name = room.getName();
		rr.maxUser = room.getMaxMemberCount();
		return rr;
	}
}
