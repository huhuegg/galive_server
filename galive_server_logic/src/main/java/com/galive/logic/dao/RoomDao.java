package com.galive.logic.dao;

import java.util.Set;

public interface RoomDao {
	
	public void removeFreeRooms();
	
	public void removeUsedRooms();
	
	public void removeUsedRoom(String room);
	
	public void saveFreeRoom(String room);
	
	public void saveUsedRoom(String room);
	
	public boolean roomUsed(String room);
	
	public String popFreeRoom();
	
	public Set<String> findUsedRooms();
	
	public Set<String> findunusedRooms();
}
