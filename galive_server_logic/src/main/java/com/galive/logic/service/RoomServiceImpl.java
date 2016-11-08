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
		// TODO Auto-generated method stub
		
	}

	@Override
	public String useFreeRoom() throws LogicException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void returnUsedRoom(String room) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> listUsedRoom() {
		// TODO Auto-generated method stub
		return null;
	}

	/*@Override
	public void saveRooms(String serverIp, int serverPort, List<String> rooms) {
		roomDao.removeFreeRooms();
		roomDao.removeUsedRooms();
		Set<String> usedRooms = roomDao.findUsedRooms();
		for (String room : rooms) {
			if (!usedRooms.contains(room)) {
				String roomName = roomName(serverIp, serverPort, room);
				roomDao.saveFreeRoom(roomName);
			}
		}
	}

	@Override
	public String getFreeRoom() throws LogicException {
		String room = roomDao.popFreeRoom();
		if (!StringUtils.isEmpty(room)) {
			roomDao.saveUsedRoom(room);
		}
		return room;
	}

	@Override
	public void returnRoom(String room) {
		roomDao.removeUsedRoom(room);
		roomDao.saveFreeRoom(room);
	}

	private String roomName(String serverIp, int serverPort, String room) {
		//String roomName = serverIp + ":" + serverPort + ":" + room;
		return room;
	}

	@Override
	public List<String> listUsedRoom() {
		List<String> result = new ArrayList<String>();
		Set<String> rooms = roomDao.findUsedRooms();
		for (String r : rooms) {
			result.add(r);
		}
		return result;
	}*/
	
	

}
