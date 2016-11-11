package com.galive.logic.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.galive.logic.dao.MeetingDao;
import com.galive.logic.dao.MeetingDaoImpl;
import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Meeting;
import com.galive.logic.model.Sid;
import com.galive.logic.model.Sid.EntitySeq;
import com.galive.logic.model.account.Account;

public class MeetingServiceImpl extends BaseService implements MeetingService {

	private MeetingDao meetingDao = new MeetingDaoImpl();
	private RoomService roomService = new RoomServiceImpl();
	private AccountService accountService = new AccountServiceImpl();
	
	
	@Override
	public Meeting createMeeting(Account account) throws LogicException {
		Meeting meeting = new Meeting();
		meeting.setAccountSid(account.getSid());
		String searchName = Sid.getNextSequence(EntitySeq.MeetingSearchName) + "";
		int len = searchName.length();
		if (len < 8) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < 8 - len; i++) {
				sb.append("0");
			}
			sb.append(searchName);
			meeting.setSearchName(sb.toString());
		} else {
			meeting.setSearchName(searchName);
		}
		meeting.setDisplayName(searchName);
		meeting = meetingDao.saveOrUpdate(meeting);
		return meeting;
	}
	
	@Override
	public Meeting startMeeting(String accountSid) throws LogicException {
		Meeting joined = meetingDao.findByMember(accountSid);
		if (joined != null) {
			throw makeLogicException("正在其他会议中.");
		}
		Meeting meeting = meetingDao.findByAccount(accountSid);
		if (!StringUtils.isEmpty(meetingDao.findRoom(meeting.getSid()))) {
			throw makeLogicException("会议已开始");
		}
		Account act = accountService.findAndCheckAccount(accountSid);
		List<Account> members = new ArrayList<>();
		List<String> memberAccounts = new ArrayList<>();
		members.add(act);
		memberAccounts.add(accountSid);
		meeting.setMembers(members);
		meeting.setMemberSids(memberAccounts);
		
		String room = roomService.useFreeRoom();
		meetingDao.bindRoom(meeting.getSid(), room);
		meeting.setRoom(room);
		meetingDao.saveOrUpdate(meeting);
		
		// 插入排行榜
		meetingDao.insertToMeetingRank(meeting.getSid());
		
		return meeting;
	}
	
	@Override
	public void stopMeeting(String accountSid) throws LogicException {
		Meeting meeting = meetingDao.findByAccount(accountSid);
		String meetingSid = meeting.getSid();
		// 释放媒体房间
		String room = meetingDao.findRoom(meetingSid);
		if (!StringUtils.isEmpty(room)) {
			roomService.returnUsedRoom(room);
		}
		meetingDao.unbindRoom(meetingSid);
		
		meeting.setMemberSids(new ArrayList<>());
		meetingDao.saveOrUpdate(meeting);
		
		meetingDao.removeFromMeetingRank(meeting.getSid());
		
	}
	
	@Override
	public Meeting joinMeeting(String accountSid, String searchName, String password) throws LogicException {
		Meeting joined = meetingDao.findByMember(accountSid);
		if (joined != null) {
			throw makeLogicException("正在其他会议中.");
		}
		Meeting selfMeeting = meetingDao.findByAccount(accountSid);
		if (selfMeeting.getSearchName().equals(searchName)) {
			throw makeLogicException("无法加入自己的会议");
		}
		Meeting meeting = meetingDao.findBySearchName(searchName);
		
		if (meeting == null) {
			throw makeLogicException("会议不存在");
		}
		
		
		String pwd = meeting.getPassword();
		if (!StringUtils.isEmpty(pwd) && !pwd.equals(password)) {
			throw makeLogicException("密码错误");
		}
		
		String room = meetingDao.findRoom(meeting.getSid());
		meeting.setRoom(room);
		List<String> memberSids = meeting.getMemberSids();
		memberSids.add(accountSid);
		meeting.setMemberSids(memberSids);
		meeting = meetingDao.saveOrUpdate(meeting);
		
		
		List<Account> members = new ArrayList<>();
		for (String actSid : memberSids) {
			Account member = accountService.findAndCheckAccount(actSid);
			members.add(member);
		}
		meeting.setMembers(members);
		return meeting;
	}
	
	@Override
	public void leaveMeeting(String accountSid) throws LogicException {
		Meeting joined = meetingDao.findByMember(accountSid);
		if (joined != null) {
			joined.getMemberSids().remove(accountSid);
			meetingDao.saveOrUpdate(joined);
		}
	}
	
	@Override
	public Meeting findMeeting(String meetingSearchName, String accountSid, String memberSid) throws LogicException {
		Meeting meeting = null;
		if (!StringUtils.isEmpty(meetingSearchName)) {
			meeting = meetingDao.findBySearchName(meetingSearchName);
		} else if (!StringUtils.isEmpty(accountSid)) {
			meeting = meetingDao.findByAccount(accountSid);
		} else if (!StringUtils.isEmpty(memberSid)) {
			meeting = meetingDao.findByMember(memberSid);
		}
		if (meeting != null) {
			String room = meetingDao.findRoom(meeting.getSid());
			meeting.setRoom(room == null ? "" : room);
			List<Account> members = new ArrayList<>();
			List<String> memberSids = meeting.getMemberSids();
//			memberSids.add(accountSid);
//			meeting.setMemberSids(memberSids);
			if (memberSids != null) {
				for (String actSid : memberSids) {
					Account member = accountService.findAndCheckAccount(actSid);
					members.add(member);
				}
			}
			
		}
		return meeting;
	}
	
	@Override
	public Meeting kickMember(String accountSid, String targetSid) throws Exception {
		if (accountSid.equals(targetSid)) {
			throw makeLogicException("无法踢出自己");
		}
		Meeting m = meetingDao.findByAccount(accountSid);
		List<String> members = m.getMemberSids();
		for (String actSid : members) {
			if (actSid.equals(targetSid)) {
				members.remove(actSid);
				break;
			}
		}
		m.setMemberSids(members);
		meetingDao.saveOrUpdate(m);
		return m;
	}
	
	@Override
	public Meeting updateMeeting(String accountSid, String displayName, String profile, String password, List<String> tags, String coverImage) throws LogicException {
		Meeting meeting = meetingDao.findByAccount(accountSid);
		if (!StringUtils.isEmpty(displayName)) {
			meeting.setDisplayName(displayName);
		}
		if (profile != null) {
			meeting.setProfile(profile);
		}
		if (password != null) {
			meeting.setPassword(password);
		}
		if (coverImage != null) {
			meeting.setCoverImage(coverImage);
		}
		meeting.setTags(tags);		
		meetingDao.saveOrUpdate(meeting);
		return meeting;
	}
	
	@Override
	public List<Meeting> listMeetings(int start, int end) throws LogicException {
		List<Meeting> meetings = meetingDao.meetings(start, end);
		for (Meeting m : meetings) {
			String room = meetingDao.findRoom(m.getSid());
			m.setRoom(room);
			
			List<Account> acts = new ArrayList<>();
			for (String actSid : m.getMemberSids()) {
				Account a = accountService.findAndCheckAccount(actSid);
				acts.add(a);
			}
			m.setMembers(acts);
		}
		return meetings;
	}

	@Override
	public List<Meeting> listStartedMeetings(int start, int end) throws LogicException {
		List<String> sids = meetingDao.meetingRank(start, end);
		List<Meeting> meetings = new ArrayList<>();
		for (String sid : sids) {
			Meeting m = meetingDao.find(sid);
			String room = meetingDao.findRoom(sid);
			m.setRoom(room);
			
			List<Account> acts = new ArrayList<>();
			for (String actSid : m.getMemberSids()) {
				Account a = accountService.findAndCheckAccount(actSid);
				acts.add(a);
			}
			m.setMembers(acts);
			meetings.add(m);
		}
		return meetings;
		
	}
	
	@Override
	public void updateShareState(String accountSid, boolean started) throws Exception {
		meetingDao.saveShareState(accountSid, started);
	}
	
	@Override
	public boolean findShareState(String accountSid) throws Exception {
		boolean started = meetingDao.findShareState(accountSid);
		return started;
	}

	
	
}
