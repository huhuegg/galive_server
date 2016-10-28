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
import com.galive.logic.model.account.Account;
import com.galive.logic.model.account.PlatformAccount;
import com.galive.logic.model.account.PlatformAccountGuest;
import com.galive.logic.model.account.PlatformAccountWeChat;

public class MeetingServiceImpl extends BaseService implements MeetingService {

	private MeetingDao meetingDao = new MeetingDaoImpl();
	private RoomService roomService = new RoomServiceImpl();
	private AccountService accountService = new AccountServiceImpl();
	
	public Meeting checkAccountInMeeting(String accountSid, boolean isInMeeting) throws LogicException {
		Meeting meeting = meetingDao.findByAccount(accountSid);
		if (isInMeeting) {
			if (meeting == null) {
				throw makeLogicException("用户不在会议中。");
			}
		} else {
			if (meeting != null) {
				throw makeLogicException(String.format("正在会议%s(%s)中。", meeting.getOptions().getName(), meeting.getId()));
			}
		}
		return meeting;
	}
	
	@Override
	public Meeting findMeeting(String meetingDisplayId, String accountSid, boolean checkNull) throws LogicException {
		Meeting meeting = null;
		
		if (!StringUtils.isEmpty(meetingDisplayId)) {
			String meetingSid = findMeetingId(meetingDisplayId);
			meeting = meetingDao.find(meetingSid);
		} else {
			if (!StringUtils.isEmpty(accountSid)) {
				meeting = meetingDao.findByAccount(accountSid);
			}
		}
		if (checkNull && meeting == null) {
			throw makeLogicException("会议未开始。");
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
			throw makeLogicException("会议设置为空。");
//			logBuffer.append("options为空，使用用户自己的会议设置");
//			Account act = accountService.findAndCheckAccount(accountSid);
//			options = act.getMeetingOptions();
//			if (options == null) {
//				options = new MeetingOptions();
//				options.setName(MeetingOptions.randomName());
//			}
//			memberOptions = act.getMeetingMemberOptions();
//			if (memberOptions == null) {
//				memberOptions = new MeetingMemberOptions();
//			}
		} 
		
		if (memberOptions == null) {
			throw makeLogicException("成员设置为空。");
		}
		
		if (StringUtils.isEmpty(options.getName())) {
			throw makeLogicException("会议名为空。");
		}
		
		holder.setOptions(memberOptions);
		meeting.setHolder(holder.getAccountSid());
		
		List<MeetingMember> members = new ArrayList<>();
		members.add(holder);
		meeting.setMembers(members);
		
		meeting.setOptions(options);
		String room = roomService.getFreeRoom();
		if (StringUtils.isEmpty(room)) {
			throw makeLogicException("房间数已达上限，无法创建更多会议。");
		}
		
		meeting.setRoom(room);
		logBuffer.append("房间:" + room);
		meeting = meetingDao.saveOrUpdate(meeting);
		
		Account act = accountService.findAndCheckAccount(accountSid);
		String displayId = act.getMeetingDisplayId();
		meetingDao.bindDisplayId(meeting.getSid(), displayId);
		meeting.setDisplayId(displayId);
		return meeting;
	}

	@Override
	public Meeting joinMeeting(String accountSid, String meetingDisplayId, String password, MeetingMemberOptions meetingMemberOptions) throws LogicException {
		checkAccountInMeeting(accountSid, false);
		Meeting meeting = findMeeting(meetingDisplayId, null, true);
		String meetingPass = meeting.getOptions().getPassword();
		if (!StringUtils.isEmpty(meetingPass) && !StringUtils.isEmpty(password) && !password.equals(meeting.getOptions().getPassword())) {
			throw makeLogicException("会议密码错误。");
		}
		
		List<MeetingMember> members = meeting.getMembers();
		if (meetingMemberOptions == null) {
			meetingMemberOptions = new MeetingMemberOptions();
		}
		MeetingMember meetingMember = new MeetingMember();
		meetingMember.setAccountSid(accountSid);
		meetingMember.setOptions(meetingMemberOptions);
		members.add(meetingMember);
		meeting.setMembers(members);
		meeting.setDisplayId(meetingDisplayId);
		meetingDao.saveOrUpdate(meeting);
		return meeting;
	}

	@Override
	public Meeting leaveMeeting(String accountSid) throws LogicException {
		checkAccountInMeeting(accountSid, true);
		Meeting meeting = findMeeting(null, accountSid, true);
		List<MeetingMember> members = meeting.getMembers();
		for (MeetingMember member : members) {
			if (member.getAccountSid().equals(accountSid)) {
				members.remove(member);
				break;
			}
		}
		if (members.isEmpty()) {
			logBuffer.append("会议成员为0，销毁会议");
			meetingDao.delete(meeting);
			roomService.returnRoom(meeting.getRoom());
			return null;
		} else {
			if (meeting.getHolder().equals(accountSid)) {
				logBuffer.append("转让主持人");
				meeting.setHolder(members.get(0).getAccountSid());
			}
			meeting.setMembers(members);
			meetingDao.saveOrUpdate(meeting);
		}
		return meeting;
	}

	@Override
	public Meeting destroyMeeting(String accountSid) throws LogicException {
		Meeting meeting = checkAccountInMeeting(accountSid, true);
		if (!meeting.getHolder().equals(accountSid)) {
			throw makeLogicException("非主持人不能解散会议。");
		}
		meetingDao.delete(meeting);
		roomService.returnRoom(meeting.getRoom());
		Account act = accountService.findAndCheckAccount(accountSid);
		String displayId = act.getMeetingDisplayId();
		meetingDao.unbundDisplayId(displayId);
		return meeting;
	}

	@Override
	public void changeHolder(String accountSid, String newHolder) throws LogicException {
		Meeting meeting = checkAccountInMeeting(accountSid, true);
		if (!meeting.getHolder().equals(accountSid)) {
			throw makeLogicException("非主持人,无法转让权限。");
		}
		List<MeetingMember> meetingMembers = meeting.getMembers();
		boolean isMember = false;
		for (MeetingMember member : meetingMembers) {
			if (member.getAccountSid().equals(newHolder)) {
				isMember = true;
				break;
			}
		}
		if (!isMember) {
			throw makeLogicException("非会议成员，无法转让。");
		}
		
		meeting.setHolder(newHolder);
		meetingDao.saveOrUpdate(meeting);
	}

	@Override
	public Meeting updateMeetingOptions(String accountSid, MeetingOptions options, boolean belongToAccount) throws LogicException {
		Meeting meeting = checkAccountInMeeting(accountSid, true);
		if (!meeting.getHolder().equals(accountSid)) {
			throw makeLogicException("非主持人,无法修改会议设置。");
		}
		if (belongToAccount) {
			options = accountService.updateMeetingOptions(accountSid, options);
		}
		meeting.setOptions(options);
		logBuffer.append("更新自己的会议设置");
		meetingDao.saveOrUpdate(meeting);
		return meeting;
	}

	@Override
	public Meeting updateMeetingMemberOptions(String accountSid, MeetingMemberOptions options, boolean belongToAccount) throws LogicException {
		Meeting meeting = checkAccountInMeeting(accountSid, true);
		if (belongToAccount) {
			options = accountService.updateMeetingMemberOptions(accountSid, options);
		}
		List<MeetingMember> members = meeting.getMembers();
		for (int i = 0, size = members.size(); i < size; i++) {
			if (accountSid.equals(members.get(i).getAccountSid())) {
				members.get(i).setOptions(options);
				break;
			}
		}
		meeting.setMembers(members);
		logBuffer.append("更新自己的会议设置");
		meetingDao.saveOrUpdate(meeting);
		return meeting;
	}

	@Override
	public Meeting kickMember(String accountSid, String targetSid) throws Exception {
		Meeting meeting = checkAccountInMeeting(accountSid, true);
		if (!meeting.getHolder().equals(accountSid)) {
			throw makeLogicException("非主持人,无法踢人。");
		}
		if (accountSid.equals(targetSid)) {
			throw makeLogicException("无法踢出自己。");
		}
		List<MeetingMember> members = meeting.getMembers();
		for (int i = 0, size = members.size(); i < size; i++) {
			if (targetSid.equals(members.get(i).getAccountSid())) {
				members.remove(i);
				break;
			}
		}
		meeting.setMembers(members);
		meetingDao.saveOrUpdate(meeting);
		return meeting;
	}

	@Override
	public List<MeetingMember> listMeetingMembersWithDetailInfo(Meeting meeting) throws Exception {
		List<MeetingMember> members = meeting.getMembers();
		for (MeetingMember m : members) {
			Account act = accountService.findAndCheckAccount(m.getAccountSid());
			PlatformAccount platformAccount = accountService.findPlatformAccount(act.getLatestLoginPlatform());
			switch (platformAccount.getPlatform()) {
			case Guest:
				PlatformAccountGuest guest = (PlatformAccountGuest) platformAccount;
				m.setNickname(guest.getName());
				m.setAvatar("");
				break;
			case WeChat:
				PlatformAccountWeChat wechat = (PlatformAccountWeChat) platformAccount;
				m.setNickname(wechat.getNickname());
				m.setAvatar(wechat.getHeadimgurl());
				break;
			}
		}
		return members;
	}


	@Override
	public void changeShareState(String accountSid, boolean started) throws Exception {
		meetingDao.saveShareState(accountSid, started);
	}

	@Override
	public boolean loadShareState(String accountSid) throws Exception {
		boolean started = meetingDao.loadShareState(accountSid);
		return started;
	}

	@Override
	public String findMeetingId(String meetingDisplayId) throws LogicException {
		String meetingSid = meetingDao.findMeetingId(meetingDisplayId);
		return meetingSid;
	}


}
