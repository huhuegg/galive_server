package com.galive.logic.service;

import java.util.List;
import com.galive.logic.dao.MeetingDao;
import com.galive.logic.dao.MeetingDaoImpl;
import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Meeting;
import com.galive.logic.model.account.Account;

public class MeetingServiceImpl extends BaseService implements MeetingService {

	private MeetingDao meetingDao = new MeetingDaoImpl();
	private RoomService roomService = new RoomServiceImpl();
	private AccountService accountService = new AccountServiceImpl();
	
	
	@Override
	public Meeting createMeeting(Account account) throws LogicException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Meeting startMeeting(String accountSid) throws LogicException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void stopMeeting(String accountSid) throws LogicException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Meeting joinMeeting(String accountSid, String searchName, String password) throws LogicException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void leaveMeeting(String accountSid) throws LogicException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Meeting findMeeting(String meetingSearchName, String accountSid) throws LogicException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Meeting kickMember(String accountSid, String targetSid) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Meeting updateMeeting(String accountSid, String displayName, String profile, String password,
			List<String> tags) throws LogicException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void updateShareState(String accountSid, boolean started) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean findShareState(String accountSid) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}
	
	/*public Meeting checkAccountInMeeting(String accountSid, boolean isInMeeting) throws LogicException {
		Meeting meeting = meetingDao.findByAccount(accountSid);
		if (isInMeeting) {
			if (meeting == null) {
				throw makeLogicException("用户不在会议中。");
			}
		} else {
			if (meeting != null) {
				throw makeLogicException(String.format("正在会议%s(%s)中。", meeting.getOptions().getDisplayName(), meeting.getId()));
			}
		}
		return meeting;
	}
	
	@Override
	public Meeting findMeeting(String meetingSearchName, String accountSid, boolean checkNull) throws LogicException {
		Meeting meeting = null;
		
		if (!StringUtils.isEmpty(meetingSearchName)) {
			meeting = meetingDao.findMeetingBySearchName(meetingSearchName);
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
	public Meeting createMeeting(String accountSid, MeetingOptions options) throws LogicException {
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
		

		if (StringUtils.isEmpty(options.getDisplayName())) {
			throw makeLogicException("会议名为空。");
		}
		
		meeting.setHolder(holder.getAccountSid());
		
		List<MeetingMember> members = new ArrayList<>();
		members.add(holder);
		meeting.setMembers(members);
		
		Account act = accountService.findAndCheckAccount(accountSid);
		MeetingOptions defaultOptions = act.getMeetingOptions();
		options.setSearchName(defaultOptions.getSearchName());
		
		meeting.setOptions(options);
		String room = roomService.getFreeRoom();
		if (StringUtils.isEmpty(room)) {
			throw makeLogicException("房间数已达上限，无法创建更多会议。");
		}
		
		meeting.setRoom(room);
		logBuffer.append("房间:" + room);
		meeting = meetingDao.saveOrUpdate(meeting);
		
		return meeting;
	}

	@Override
	public Meeting joinMeeting(String accountSid, String searchName, String password) throws LogicException {
		checkAccountInMeeting(accountSid, false);
		Meeting meeting = findMeeting(searchName, null, true);
		String meetingPass = meeting.getOptions().getPassword();
		if (!StringUtils.isEmpty(meetingPass) && !StringUtils.isEmpty(password) && !password.equals(meeting.getOptions().getPassword())) {
			throw makeLogicException("会议密码错误。");
		}
		
		List<MeetingMember> members = meeting.getMembers();

		MeetingMember meetingMember = new MeetingMember();
		meetingMember.setAccountSid(accountSid);
		members.add(meetingMember);
		meeting.setMembers(members);
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
			m.setNickname(act.getNickname());
			m.setAvatar(act.getAvatar());
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
*/
}
