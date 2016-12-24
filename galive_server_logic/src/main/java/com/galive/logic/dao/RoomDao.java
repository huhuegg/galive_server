package com.galive.logic.dao;

import com.galive.logic.model.Room;

public interface RoomDao {
	
	
	
	public Room findBySid(String sid);

	public Room findByOwner(String sid);
	
	public Room findByMember(String sid);
	
	public Room save(Room room);
	
	public void removeMember(String memberSid);
	
	public void delete(Room room);
}
 