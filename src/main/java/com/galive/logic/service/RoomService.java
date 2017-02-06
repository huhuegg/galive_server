package com.galive.logic.service;

import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Room;

import java.util.Map;


public interface RoomService {

	enum FindRoomBy {
		id,
		Owner,
		Member
	}

	Room findRoom(FindRoomBy by, String byId) throws LogicException;
	
	Room createRoom(String accountSid) throws LogicException;
	
	Room joinRoom(String roomSid, String accountSid) throws LogicException;
	
	Room leaveRoom(String accountSid) throws LogicException;
	
	Room destroyRoom(String accountSid) throws LogicException;

	Room updateRoomExtraInfo(Room room, Map<String, Object> extraInfo) throws LogicException;

	Room bindPCClient(String accountSid, String pcClientId) throws LogicException;


}
