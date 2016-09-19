package com.galive.logic.dao;

import com.galive.logic.model.Meeting;

public interface MeetingDao {
	
	public Meeting saveOrUpdate(Meeting meeting);
	
	public Meeting find(String meetingSid);
	
	public Meeting delete(Meeting meeting);
	
	/**
	 * 查找用户所在会议
	 * @param accountSid
	 * @return
	 */
	public Meeting findByAccount(String accountSid);
	
	
}
 