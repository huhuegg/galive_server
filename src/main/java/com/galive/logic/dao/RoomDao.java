package com.galive.logic.dao;

import java.util.Set;

import com.galive.logic.model.Room;

public interface RoomDao {
	
	public Set<String> findAllRooms();
	
	public Room findBySid(String sid);

	public Room findByOwner(String sid);
	
	public Room findByMember(String sid);
	
	public Room save(Room room);
	
	public void removeMember(String memberSid);
	
	public void delete(Room room);
}
 