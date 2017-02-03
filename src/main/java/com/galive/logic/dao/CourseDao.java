package com.galive.logic.dao;

import com.galive.logic.model.Room;

import java.util.Set;

public interface CourseDao {
	
	public Set<String> findAllRooms();
	
	public Room findBySid(String sid);

	public Room findByOwner(String sid);
	
	public Room findByMember(String sid);
	
	public Room save(Room room);
	
	public void removeMember(String memberSid);
	
	public void delete(Room room);
}
 