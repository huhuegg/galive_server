package com.galive.logic.service;

import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Meeting;
import com.galive.logic.model.MeetingMemberOptions;
import com.galive.logic.model.MeetingOptions;

public interface MeetingService {

	/**
	 * 查找会议
	 * @param meetingSid 会议id 二选一
	 * @param accountSid 用户所在的会议 可选 二选一
	 * @return
	 * @throws LogicException
	 */
	public Meeting findMeeting(String meetingSid, String accountSid, boolean checkNull) throws LogicException;

	/**
	 * 创建房间 
	 * @param accountSid
	 * @param options 房间设置 null则使用用户默认设置
	 * @param memberOptions 房间中用户设置
	 * @return
	 * @throws LogicException
	 */
	public Meeting createMeeting(String accountSid, MeetingOptions options, MeetingMemberOptions memberOptions) throws LogicException;

	public Meeting joinMeeting(String accountSid, String meetingSid, MeetingMemberOptions meetingMemberOptions) throws LogicException;

	public Meeting leaveMeeting(String accountSid, String meetingSid) throws LogicException;

	public void destroyMeeting(String accountSid) throws LogicException;

	public void leaveMeeting(String accountSid) throws LogicException;

	public void changeHolder(String accountSid, String holderSid) throws LogicException;

	public void shareFile(String accountSid, String fileUrl) throws LogicException;

	public void transmit(String accountSid, String content) throws LogicException;

	public void updateMeetingOptions(String accountSid, MeetingOptions options) throws LogicException;

	public void updateMeetingMemberOptions(String accountSid, MeetingMemberOptions options) throws LogicException;

	public void kickMember(String accountSid, String targetSid) throws Exception;

	public boolean isMeetingMember(String accountSid) throws Exception;

}
