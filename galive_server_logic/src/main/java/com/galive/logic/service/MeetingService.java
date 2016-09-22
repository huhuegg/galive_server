package com.galive.logic.service;

import java.util.List;

import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Meeting;
import com.galive.logic.model.MeetingMember;
import com.galive.logic.model.MeetingMemberOptions;
import com.galive.logic.model.MeetingOptions;

public interface MeetingService {

	/**
	 * 检查用户是否在会议中
	 * @param accountSid
	 * @param isInMeeting 检查在/不在会议中
	 * @return
	 * @throws LogicException
	 */
	public Meeting checkAccountInMeeting(String accountSid, boolean isInMeeting) throws LogicException;
	
	/**
	 * 查找会议
	 * @param meetingSid 会议id 二选一
	 * @param accountSid 用户所在的会议 可选 二选一
	 * @return
	 * @throws LogicException
	 */
	public Meeting findMeeting(String meetingSid, String accountSid, boolean checkNull) throws LogicException;

	/**
	 * 创建会议
	 * @param accountSid
	 * @param options 会议设置 null则使用用户默认设置
	 * @param memberOptions 会议中用户设置
	 * @return
	 * @throws LogicException
	 */
	public Meeting createMeeting(String accountSid, MeetingOptions options, MeetingMemberOptions memberOptions) throws LogicException;

	public Meeting joinMeeting(String accountSid, String meetingSid, MeetingMemberOptions meetingMemberOptions) throws LogicException;

	/**
	 * 离开会议
	 * @param accountSid
	 * @return 空则说明会议被销毁
	 * @throws LogicException
	 */
	public Meeting leaveMeeting(String accountSid) throws LogicException;

	/**
	 * 解散会议 仅主持人操作
	 * @param accountSid
	 * @throws LogicException
	 */
	public Meeting destroyMeeting(String accountSid) throws LogicException;

	public void changeHolder(String accountSid, String newHolder) throws LogicException;

	public Meeting updateMeetingOptions(String accountSid, MeetingOptions options, boolean belongToAccount) throws LogicException;

	public Meeting updateMeetingMemberOptions(String accountSid, MeetingMemberOptions options, boolean belongToAccount) throws LogicException;

	public Meeting kickMember(String accountSid, String targetSid) throws Exception;
	
	public List<MeetingMember> listMeetingMembersWithDetailInfo(Meeting meeting) throws Exception;

}
