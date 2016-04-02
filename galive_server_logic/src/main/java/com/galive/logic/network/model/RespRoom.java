package com.galive.logic.network.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.galive.logic.model.Room;
import com.galive.logic.model.User;

public class RespRoom {

	public String name = "";
	
	public String sid = "";
	
	public String ownerId = "";
	
	public int maxUser = 2;
	
	public List<RespUser> users = new ArrayList<>();
	
	public static RespRoom convertFromUserRoom(Room room) {
		RespRoom rr = new RespRoom();
		rr.sid = room.getRoomId();
		rr.ownerId = room.getOwnerId();
		rr.name = room.getName();
		rr.maxUser = room.getMaxMemberCount();
		Set<String> users = room.getUsers();
		for (String uid : users) {
			User u = User.findBySid(uid);
			if (u != null) {
				RespUser ru = RespUser.convertFromUser(u);
				rr.users.add(ru);
			}
		}
		return rr;
	}
}
