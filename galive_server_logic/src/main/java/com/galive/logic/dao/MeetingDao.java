package com.galive.logic.dao;

import com.galive.logic.model.Meeting;

public interface MeetingDao {
	
	/**
	 * 保存/更新会议
	 * @param meeting
	 * @return
	 */
	public Meeting saveOrUpdate(Meeting meeting);
	
	/**
	 * 根据id查找会议
	 * @param meetingSid
	 * @return
	 */
	public Meeting find(String meetingSid);
	
	/**
	 * 根据searchName查找会议
	 * @param meetingSearchName
	 * @return
	 */
	public Meeting findBySearchName(String meetingSearchName);
	
	/**
	 * 查找用户会议
	 * @param accountSid
	 * @return
	 */
	public Meeting findByAccount(String accountSid);
	
	/**
	 * 查找用户所在的会议
	 * @param accountSid
	 * @return
	 */
	public Meeting findByMember(String accountSid);
	
	/**
	 * 绑定语音服务器房间
	 * @param meetingSid
	 * @param room
	 */
	public void bindRoom(String meetingSid, String room);
	
	/**
	 * 解绑定语音服务器房间
	 * @param meetingSid
	 */
	public void unbindRoom(String meetingSid);
	
	/**
	 * 保存屏幕分享状态
	 * @param accountSid
	 * @param start
	 */
	public void saveShareState(String accountSid, boolean start);
	
	/**
	 * 查找用户分享状态
	 * @param accountSid
	 * @return
	 */
	public boolean findShareState(String accountSid);
	
	
}
 