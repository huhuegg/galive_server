package com.galive.logic.dao;

import com.galive.logic.db.RedisManager;


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

	

}
