package com.galive.logic.service;

import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Room;

public class RoomServiceImpl extends BaseService implements RoomService {

	@Override
	public Room findRoom(FindRoomBy by, String byId) throws LogicException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Room createRoom(String accountSid) throws LogicException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Room joinRoom(String roomSid, String accountSid) throws LogicException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Room leaveRoom(String accountSid) throws LogicException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Room destroyRoom(String accountSid) throws LogicException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateScreenShareState(String accountSid, boolean started) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean findScreenShareState(String accountSid) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	//private MeetingDao meetingDao = new MeetingDaoImpl();
	
}
