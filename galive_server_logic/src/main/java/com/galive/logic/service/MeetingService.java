package com.galive.logic.service;

import java.util.List;
import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Meeting;
import com.galive.logic.model.account.Account;

public interface MeetingService {

	/**
	 * 为用户创建会议
	 * @param account
	 * @return
	 * @throws LogicException
	 */
	public Meeting createMeeting(Account account) throws LogicException;
	
	/**
	 * 用户开始会议
	 * @param accountSid
	 * @return
	 * @throws LogicException
	 */
	public Meeting startMeeting(String accountSid) throws LogicException;
	
	/**
	 * 用户结束会议
	 * @param accountSid
	 * @throws LogicException
	 */
	public void stopMeeting(String accountSid) throws LogicException;
	
	/**
	 * 用户加入会议
	 * @param accountSid
	 * @param meetingSid
	 * @param password
	 * @return
	 * @throws LogicException
	 */
	public Meeting joinMeeting(String accountSid, String meetingSid, String password) throws LogicException;

	/**
	 * 离开会议
	 * @param accountSid
	 * @throws LogicException
	 */
	public void leaveMeeting(String accountSid) throws LogicException;
	
	/**
	 * 查找会议
	 * @param meetingSearchName 二选一
	 * @param accountSid 二选一
	 * @param memberSid 三选一
	 * @return
	 * @throws LogicException
	 */
	public Meeting findMeeting(String meetingSearchName, String accountSid, String memberSid) throws LogicException;
	
	/**
	 * 踢人
	 * @param accountSid
	 * @param targetSid
	 * @return
	 * @throws Exception
	 */
	public Meeting kickMember(String accountSid, String targetSid) throws Exception;
	
	/**
	 * 更新会议信息
	 * @param accountSid
	 * @param displayName 
	 * @param profile
	 * @param password
	 * @param tags
	 * @return
	 * @throws LogicException
	 */
	public Meeting updateMeeting(String accountSid, String displayName, String profile, String password, List<String> tags) throws LogicException;
	
	/**
	 * 会议列表
	 * @param start
	 * @param end
	 * @return
	 * @throws LogicException
	 */
	public List<Meeting> listMeetings(int start, int end) throws LogicException;
	
	/**
	 * 已开始会议列表
	 * @param start
	 * @param end
	 * @return
	 * @throws LogicException
	 */
	public List<Meeting> listStartedMeetings(int start, int end) throws LogicException;
	
	
	/**
	 * 更新屏幕分享状态
	 * @param accountSid
	 * @param started
	 * @throws Exception
	 */
	public void updateShareState(String accountSid, boolean started) throws Exception; 
	
	/**
	 * 查找屏幕分享状态
	 * @param accountSid
	 * @return
	 * @throws Exception
	 */
	public boolean findShareState(String accountSid) throws Exception; 

}
