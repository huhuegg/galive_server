package com.galive.logic.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
		roomDao.removeFreeRoom();
		roomDao.removeUsedRoom();
		for (String r : rooms) {
			String room = roomName(serverIp, serverPort, r);
			roomDao.saveFreeRoom(room);
		}
	}

	@Override
	public String getFreeRoom() throws LogicException {
		String room = roomDao.popFreeRoom();
		return room;
	}

	@Override
	public void returnRoom(String room) {
		roomDao.saveFreeRoom(room);
	}

	private String roomName(String serverIp, int serverPort, String room) {
		String roomName = serverIp + ":" + serverPort + ":" + room;
		return roomName;
	}

	@Override
	public List<String> listUsedRoom() {
		List<String> result = new ArrayList<String>();
		Set<String> rooms = roomDao.findUsedRooms();
		for (String r : rooms) {
			result.add(r);
		}
		return result;
	}
	
	

}
