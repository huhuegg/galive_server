package com.galive.logic.service;

import java.util.List;

import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Room;

public interface RoomService {

	public Room findRoomByUser(String userSid);
	
	public List<Room> list(int index, int size);
	
	public Room create(String roomname, String UserSid, List<String> invitedUsers, int maxUser) throws LogicException;
	
	public Room exit(String userSid) throws LogicException;
	
	public Room enter(String roomSid, String userSid) throws LogicException;
}
