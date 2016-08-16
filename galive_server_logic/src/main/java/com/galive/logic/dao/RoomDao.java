package com.galive.logic.dao;

import java.util.Set;

public interface RoomDao {
	
	public void removeFreeRoom();
	
	public void removeUsedRoom();
	
	public void saveFreeRoom(String room);
	
	public void saveUsedRoom(String room);
	
	public boolean roomUsed(String room);
	
	public String popFreeRoom();
	
	public Set<String> findUsedRooms();
}
