package com.galive.logic.service;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.galive.logic.dao.RoomDao;
import com.galive.logic.dao.RoomDaoImpl;
import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Room;
import com.galive.logic.network.socket.ChannelManager;

public class RoomServiceImpl extends BaseService implements RoomService {

	private RoomDao roomDao = new RoomDaoImpl();
	
	static {
		ExecutorService thread = Executors.newSingleThreadExecutor();
		thread.execute(new Runnable() {
			
			@Override
			public void run() {
				while (true) {
					RoomDao roomDao = new RoomDaoImpl();
					Set<String> rooms = roomDao.findAllRooms();
					if (rooms != null) {
						for (String id : rooms) {
							Room room = roomDao.findBySid(id);
							if (room != null) {
								boolean allOffline = true;
								for (String member : room.getMembers()) {
									if (ChannelManager.getInstance().isOnline(member)) {
										allOffline = false;
										break;
									}
								}
								
								if (allOffline) {
									roomDao.delete(room);
								}
							} 
						}
					}
					
					try {
						Thread.sleep(1000 * 10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
		});
	}
	
	@Override
	public Room findRoom(FindRoomBy by, String byId) throws LogicException {
		Room room = null;
		switch (by) {
		case id:
			room = roomDao.findBySid(byId);
			break;
		case Owner:
			room = roomDao.findByOwner(byId);
			break;
		case Member:
			room = roomDao.findByMember(byId);
		}
		return room;
	}

	@Override
	public Room createRoom(String accountSid) throws LogicException {
		checkInRoom(accountSid);
		Room room = new Room();
		room.setOwnerSid(accountSid);
		room = roomDao.save(room);
		return room;
	}

	@Override
	public Room joinRoom(String roomSid, String accountSid) throws LogicException {
		checkInRoom(accountSid);
		Room room = roomDao.findBySid(roomSid);
		if (room == null) {
			throw makeLogicException("房间不存在");
		}
		room.getMembers().add(accountSid);
		room = roomDao.save(room);
		return room;
	}

	@Override
	public Room leaveRoom(String accountSid) throws LogicException {
		Room room = roomDao.findByMember(accountSid);
		if (room != null) {
			if (room.getOwnerSid().equals(accountSid)) {
				throw makeLogicException("房主无法离开房间");
			}
			room.getMembers().remove(accountSid);
			roomDao.save(room);
			roomDao.removeMember(accountSid);
			if (room.getMembers().isEmpty()) {
				roomDao.delete(room);
			}
		}
		return room;
	}

	@Override
	public Room destroyRoom(String accountSid) throws LogicException {
		Room room = roomDao.findByOwner(accountSid);
		if (room != null) {
			roomDao.delete(room);
		}
		return room;
	}

	@Override
	public Room updateScreenShareState(String accountSid, boolean started) throws Exception {
		Room room = roomDao.findByOwner(accountSid);
		if (room != null) {
			room.setScreenShared(started);
			room = roomDao.save(room);
		}
		return room;
	}
	
	private void checkInRoom(String accountSid) throws LogicException {
		Room room = roomDao.findByOwner(accountSid);
		if (room == null) {
			room = roomDao.findByMember(accountSid);
		}
		if (room != null) {
			throw makeLogicException("已在房间中");
		}
	}
	
	
}
