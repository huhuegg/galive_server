package com.galive.logic.service;

import java.util.List;

import com.galive.logic.dao.RoomDao;
import com.galive.logic.dao.RoomDaoImpl;
import com.galive.logic.exception.LogicException;

public class RoomServiceImpl extends BaseService implements RoomService {

	private RoomDao roomDao = new RoomDaoImpl();
	
	public RoomServiceImpl() {
		super();
		appendLog("RoomServiceImpl");
	}

	@Override
	public void saveRooms(String serverIp, int serverPort, List<String> rooms) {
		for (String r : rooms) {
			String room = roomName(serverIp, serverPort, r);
			roomDao.saveRoom(room);
		}
	}

	@Override
	public String getFreeRoom() throws LogicException {
		String room = roomDao.getRandomRoom();
		return room;
	}

	@Override
	public void returnRoom(String room) {
		roomDao.saveRoom(room);
	}

	private String roomName(String serverIp, int serverPort, String room) {
		String roomName = serverIp + ":" + serverPort + ":" + room;
		return roomName;
	}
	
	

}
