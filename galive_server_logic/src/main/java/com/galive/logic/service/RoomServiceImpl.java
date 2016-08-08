package com.galive.logic.service;

import java.util.List;

import com.galive.logic.exception.LogicException;

public class RoomServiceImpl extends BaseService implements RoomService {

	
	public RoomServiceImpl() {
		super();
		appendLog("RoomServiceImpl");
	}

	@Override
	public void saveRooms(String serverIp, int serverPort, List<String> rooms) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getFreeRoom() throws LogicException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void returnRoom(String room) {
		// TODO Auto-generated method stub
		
	}

	

}
