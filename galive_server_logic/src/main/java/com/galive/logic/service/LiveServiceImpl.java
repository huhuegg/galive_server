package com.galive.logic.service;

import java.util.List;
import com.galive.logic.dao.LiveDao;
import com.galive.logic.dao.LiveDaoImpl;
import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Live;

public class LiveServiceImpl extends BaseService implements LiveService {

	private LiveDao liveDao = new LiveDaoImpl();
	private RoomService roomService = new RoomServiceImpl();

	public LiveServiceImpl() {
		super();
		appendLog("LiveServiceImpl");
	}

	@Override
	public Live createLive(String account) throws LogicException {
//		checkInLive(account);		
		String liveSid = roomService.getFreeRoom();
		int unusedRoomCount = roomService.unusedRoomCount();
		appendLog("可用房间数:" + unusedRoomCount);
		if (liveSid == null) {
			appendLog("房间已满，无法再创建更多的房间。");
			throw new LogicException("无可用的房间");
		}
		
		liveDao.saveLiveCreator(liveSid, account);
		liveDao.saveLiveForCreator(liveSid, account);
		
		Live live = new Live();
		live.setSid(liveSid);
		live.setOwnerAccount(account);
		
		return live;
	}

	@Override
	public Live joinLive(String account, String liveSid) throws LogicException {
		//checkInLive(account);
		
//		boolean exist = liveDao.liveExsit(liveSid);
//		if (!exist) {
//			appendLog("房间不存在");
//			throw new LogicException("房间不存在");
//		}
		
		List<String> members = liveDao.findLiveMembers(liveSid);
		
		liveDao.saveLiveMember(liveSid, account);
		liveDao.saveLiveForMember(liveSid, account);
		
		String creator = liveDao.findLiveCreator(liveSid);
		
		members.add(account);
		Live live = new Live();
		live.setSid(liveSid);
		live.setOwnerAccount(creator);
		live.setMemberAccounts(members);
		return live;
	}

	@Override
	public Live leaveLive(String account) throws LogicException {
		String liveSid = liveDao.findLiveByMember(account);
//		if (liveSid == null) {
//			appendLog("不在房间中。");
//			throw new LogicException("不在房间中。");
//		}
		String owner = liveDao.findLiveCreator(liveSid);
		
		List<String> members = liveDao.removeLiveMember(liveSid, account);
		liveDao.removeLiveForMember(liveSid, account);
		
		Live live = new Live();
		live.setSid(liveSid);
		live.setOwnerAccount(owner);
		live.setMemberAccounts(members);
		return live;
	}

	@Override
	public Live destroyLive(String account) throws LogicException {
		String liveSid = liveDao.findLiveByCreator(account);
//		if (liveSid == null) {
//			appendLog("不在房间中。");
//			throw new LogicException("不在房间中。");
//		}
		
		List<String> members = liveDao.removeLiveMembers(liveSid);
		liveDao.removeLiveCreator(liveSid);
		liveDao.removeLiveForCreator(account);
		roomService.returnRoom(liveSid);
		
		Live live = new Live();
		live.setSid(liveSid);
		live.setOwnerAccount(account);
		live.setMemberAccounts(members);
		return live;
	}

	@Override
	public List<String> listLiveMembersByAccount(String account) throws LogicException {
		String liveSid = liveDao.findLiveByCreator(account);
		if (liveSid == null) {
			appendLog("不在房间中。");
			throw new LogicException("不在房间中。");
		}
		List<String> members = liveDao.findLiveMembers(liveSid);
		return members;
	}

	private void checkInLive(String account) throws LogicException {
		String live = liveDao.findLiveByCreator(account);
		if (live == null) {
			live = liveDao.findLiveByMember(account);
		}
		if (live != null) {
			throw new LogicException("已在房间中。");
		}
	}

	@Override
	public void clearLiveForAccount(String account) throws LogicException {
		String liveSid = liveDao.findLiveByMember(account);
		if (liveSid != null) {
			appendLog(account + "用户为房间成员，离开房间" + liveSid);
			liveDao.removeLiveMember(liveSid, account);
			liveDao.removeLiveForMember(liveSid, account);
		}
		liveSid = liveDao.findLiveByCreator(account);
		if (liveSid != null) {
			appendLog(account + "用户为房主，退出房间" + liveSid);
			liveDao.removeLiveMembers(liveSid);
			liveDao.removeLiveCreator(liveSid);
			liveDao.removeLiveForCreator(account);
			roomService.returnRoom(liveSid);
		}
		
	}


}
