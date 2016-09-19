package com.galive.logic.service;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.galive.logic.dao.MeetingDao;
import com.galive.logic.dao.MeetingDaoImpl;
import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Meeting;
import com.galive.logic.model.MeetingMember;
import com.galive.logic.model.MeetingMemberOptions;
import com.galive.logic.model.MeetingOptions;

public class MeetingServiceImpl extends BaseService implements MeetingService {

	private MeetingDao meetingDao = new MeetingDaoImpl();
	private RoomService roomService = new RoomServiceImpl();
	
	private void checkAccountInMeeting(String accountSid, boolean isInMeeting) throws LogicException {
		Meeting meeting = meetingDao.findByAccount(accountSid);
		if (isInMeeting) {
			if (meeting == null) {
				throw makeLogicException("用户不在会议中。");
			}
		} else {
			if (meeting != null) {
				throw makeLogicException(String.format("正在房间%s(%s)中。", meeting.getOptions().getName(), meeting.getId()));
			}
		}
	}
	
	@Override
	public Meeting findMeeting(String meetingSid, String accountSid, boolean checkNull) throws LogicException {
		Meeting meeting = null;
		if (!StringUtils.isEmpty(meetingSid)) {
			meeting = meetingDao.find(meetingSid);
		} else {
			if (!StringUtils.isEmpty(meetingSid)) {
				meeting = meetingDao.findByAccount(accountSid);
			}
		}
		if (checkNull && meeting == null) {
			throw makeLogicException("房间不存在。");
		}
		return meeting;
	}

	@Override
	public Meeting createMeeting(String accountSid, MeetingOptions options, MeetingMemberOptions memberOptions) throws LogicException {
		checkAccountInMeeting(accountSid, false);
		Meeting meeting = new Meeting();
		
		MeetingMember holder = new MeetingMember();
		holder.setAccountSid(accountSid);
		logBuffer.append("会议主持人:" + accountSid);

		if (options == null) {
			logBuffer.append("options为空，使用用户自己的房间设置");
			options = meetingDao.findCustomMeetingOptinsByAccount(accountSid);
			if (options == null) {
				options = new MeetingOptions();
				options.setName(MeetingOptions.randomName());
			}
			memberOptions = meetingDao.findCustomMeetingMemberOptinsByAccount(accountSid);
			if (memberOptions == null) {
				memberOptions = new MeetingMemberOptions();
			}
		} else {
			if (StringUtils.isEmpty(options.getName())) {
				throw makeLogicException("会议名为空。");
			}
		}
		
		holder.setOptions(memberOptions);
		meeting.setHolder(holder);
		
		List<MeetingMember> members = new ArrayList<>();
		members.add(holder);
		meeting.setMembers(members);
		
		meeting.setOptions(options);
		String room = roomService.getFreeRoom();
		meeting.setRoom(room);
		logBuffer.append("房间:" + room);
		meetingDao.saveOrUpdate(meeting);
		
		return meeting;
	}

	@Override
	public Meeting joinMeeting(String accountSid, String meetingSid, MeetingMemberOptions meetingMemberOptions) throws LogicException {
		checkAccountInMeeting(accountSid, false);
		Meeting meeting = findMeeting(meetingSid, null, true);
		List<MeetingMember> members = meeting.getMembers();
		if (meetingMemberOptions == null) {
			meetingMemberOptions = new MeetingMemberOptions();
		}
		MeetingMember meetingMember = new MeetingMember();
		meetingMember.setOptions(meetingMemberOptions);
		members.add(meetingMember);
		meeting.setMembers(members);
		return meeting;
	}

	@Override
	public Meeting leaveMeeting(String accountSid, String meetingSid) throws LogicException {
		checkAccountInMeeting(accountSid, true);
		Meeting meeting = findMeeting(meetingSid, null, true);
		List<MeetingMember> members = meeting.getMembers();
		for (MeetingMember member : members) {
			if (member.getAccountSid().equals(accountSid)) {
				// TODO 主持人权限
				break;
			}
		}
		meeting.setMembers(members);
		return meeting;
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
	public void updateMeetingOptions(String accountSid, MeetingOptions options) throws LogicException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateMeetingMemberOptions(String accountSid, MeetingMemberOptions options) throws LogicException {
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
