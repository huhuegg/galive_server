package com.galive.logic.dao;

import com.galive.logic.model.Meeting;
import com.galive.logic.model.MeetingMemberOptions;
import com.galive.logic.model.MeetingOptions;

public interface MeetingDao {
	
	public Meeting saveOrUpdate(Meeting meeting);
	
	public Meeting find(String meetingSid);
	
	/**
	 * 查找用户所在房间
	 * @param accountSid
	 * @return
	 */
	public Meeting findByAccount(String accountSid);
	
	/**
	 * 查找用户默认房间配置
	 * @param accountSid
	 * @return
	 */
	public MeetingOptions findCustomMeetingOptinsByAccount(String accountSid);
	
	/**
	 * 查找用户默认房间成员配置
	 * @param accountSid
	 * @return
	 */
	public MeetingMemberOptions findCustomMeetingMemberOptinsByAccount(String accountSid);
	
}
 