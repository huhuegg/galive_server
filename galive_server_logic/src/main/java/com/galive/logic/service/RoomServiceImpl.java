package com.galive.logic.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

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
		roomDao.deleteFrees();
		roomDao.deleteUseds();
		Set<String> usedRooms = roomDao.findUseds();
		for (String room : rooms) {
			if (!usedRooms.contains(room)) {
				String roomName = roomName(serverIp, serverPort, room);
				roomDao.saveFree(roomName);
			}
		}
	}

	@Override
	public String useFreeRoom() throws LogicException {
		String room = roomDao.popFree();
		if (StringUtils.isEmpty(room)) {
			throw makeLogicException("暂无可用的媒体服务器");
		}
		roomDao.saveUsed(room);
		return room;
	}

	@Override
	public void returnUsedRoom(String room) {
		roomDao.deleteUsed(room);
		roomDao.saveFree(room);
	}

	@Override
	public List<String> listUsedRoom() {
		List<String> result = new ArrayList<String>();
		Set<String> rooms = roomDao.findUseds();
		for (String r : rooms) {
			result.add(r);
		}
		return result;
	}
	
	private String roomName(String serverIp, int serverPort, String room) {
		//String roomName = serverIp + ":" + serverPort + ":" + room;
		return room;
	}

	
	
	

}
