package com.galive.logic.service;

import java.util.List;

import com.galive.logic.exception.LogicException;

public interface RoomService {
	
	public void saveRooms(String serverIp, int serverPort, List<String> rooms);
	
	public String getFreeRoom() throws LogicException;
	
	public void  returnRoom(String room);
}
	