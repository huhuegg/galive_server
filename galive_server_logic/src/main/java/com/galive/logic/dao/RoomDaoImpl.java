package com.galive.logic.dao;

import com.galive.logic.db.RedisManager;
import com.galive.logic.model.Room;


public class RoomDaoImpl extends BaseDao implements RoomDao {

	private String shareStartedKey() {
		return RedisManager.getInstance().keyPrefix() + "meeting:share_started";
	}
	
	private String meetingRoomKey(String meetingSid) {
		return RedisManager.getInstance().keyPrefix() + "meeting:room:" + meetingSid;
	}
	
	private String meetingRankByStartTimeKey() {
		return RedisManager.getInstance().keyPrefix() + "meeting:list:start_time";
	}

	
	public String generateRoomSid() {
		return "";
	}
	
	@Override
	public Room findBySid(String sid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Room findByOwner(String sid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Room findByMember(String sid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Room save(Room room) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeMember(String memberSid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Room room) {
		// TODO Auto-generated method stub
		
	}

	
}
