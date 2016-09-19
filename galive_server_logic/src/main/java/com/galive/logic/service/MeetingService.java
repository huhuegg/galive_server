package com.galive.logic.service;

import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Meeting;
import com.galive.logic.model.MeetingMemberOptions;
import com.galive.logic.model.MeetingOptions;

public interface MeetingService {
	
	public Meeting findMeetingByAccount(String accountSid) throws LogicException;
	
	public Meeting findMeeting(String meetingSid) throws LogicException;

	public Meeting createMeeting(String accountSid, String name, boolean useOwnerRoom, MeetingOptions options) throws LogicException;
	
	public Meeting joinMeeting(String accountSid, String meetingSid) throws LogicException;
	
	public Meeting leaveMeeting(String accountSid, String meetingSid) throws LogicException;

	public void destroyMeeting(String accountSid) throws LogicException;

	public void leaveMeeting(String accountSid) throws LogicException;

	public void changeHolder(String accountSid, String holderSid) throws LogicException;

	public void shareFile(String accountSid, String fileUrl) throws LogicException;

	public void transmit(String accountSid, String content) throws LogicException;
	
	public void changeMeetingOptions(String accountSid, MeetingOptions options) throws LogicException;

	public void changeMeetingMemberOptions(String accountSid, MeetingMemberOptions options) throws LogicException;
	
	public void kickMember(String accountSid, String targetSid) throws Exception;
	
	public boolean isMeetingMember(String accountSid) throws Exception;
	
}
