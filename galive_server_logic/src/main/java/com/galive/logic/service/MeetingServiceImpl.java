package com.galive.logic.service;

import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Meeting;
import com.galive.logic.model.MeetingMemberOptions;
import com.galive.logic.model.MeetingOptions;

public class MeetingServiceImpl implements MeetingService {

	@Override
	public Meeting findMeetingByAccount(String accountSid) throws LogicException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Meeting findMeeting(String meetingSid) throws LogicException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Meeting createMeeting(String accountSid, String name, boolean useOwnerRoom, MeetingOptions options)
			throws LogicException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Meeting joinMeeting(String accountSid, String meetingSid) throws LogicException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Meeting leaveMeeting(String accountSid, String meetingSid) throws LogicException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void destroyMeeting(String accountSid) throws LogicException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void leaveMeeting(String accountSid) throws LogicException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changeHolder(String accountSid, String holderSid) throws LogicException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shareFile(String accountSid, String fileUrl) throws LogicException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void transmit(String accountSid, String content) throws LogicException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changeMeetingOptions(String accountSid, MeetingOptions options) throws LogicException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changeMeetingMemberOptions(String accountSid, MeetingMemberOptions options) throws LogicException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void kickMember(String accountSid, String targetSid) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isMeetingMember(String accountSid) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
