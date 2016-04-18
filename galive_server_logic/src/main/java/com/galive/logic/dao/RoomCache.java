package com.galive.logic.dao;

import java.util.List;

import com.galive.logic.model.Room;

public interface RoomCache {

	public Room saveRoom(Room room);

	public Room findRoom(String roomSid);

	public Room findRoomByUser(String userSid);
	
	public Room findRoomByInvitee(String inviteeUserSid);

	public void deleteRoom(Room room);
	
	public void addRoomToUser(String roomSid, String userSid);
	
	public void addRoomToInvitee(String roomSid, String inviteeUserSid);

	public void removeRoomToUser(String userSid);

	public void removeRoomToInvitee(String inviteeUserSid);
	
	public List<String> listByCreateTime(int start, int end);

	public void insertToRoomListByCreateTime(String roomSid);

	public void removeFromListByCreateTime(String roomSid);

	
	
	public void updateRoomExpire(String roomSid);

	public void updateUserInRoomExpire(String userSid);

	

}
