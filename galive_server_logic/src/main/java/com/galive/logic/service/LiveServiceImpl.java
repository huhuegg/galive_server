package com.galive.logic.service;

import java.util.ArrayList;
import java.util.List;
import com.galive.logic.config.ApplicationConfig;
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
		checkInLive(account);
		
		String liveSid = roomService.getFreeRoom();
		if (liveSid == null) {
			List<String> rooms = new ArrayList<String>();
			for (int i = 0; i < 30; i++) {
				rooms.add("room" + i);
			}
			roomService.saveRooms("192.168.0.1", 4050, rooms);
			liveSid = roomService.getFreeRoom();
//			appendLog("房间已满，无法再创建更多的房间。");
//			throw new LogicException("房间已满，无法再创建更多的房间");
		}
		
		liveDao.saveLiveOwner(liveSid, account);
		
		Live live = new Live();
		live.setSid(liveSid);
		live.setOwnerAccount(account);
		
		return live;
	}

	@Override
	public Live joinLive(String account, String liveSid) throws LogicException {
		checkInLive(account);
		
		String owner = liveDao.findLiveOwner(liveSid);
		if (owner == null) {
			appendLog("房间不存在");
			throw new LogicException("房间不存在。");
		}
		
		List<String> members = liveDao.findLiveMembers(liveSid);
		if (members.size() >= ApplicationConfig.getInstance().getLogicConfig().getMaxLiveMember()) {
			throw new LogicException("该房间已满员。");
		}
		liveDao.saveLiveMember(liveSid, account);
		members.add(account);
		Live live = new Live();
		live.setSid(liveSid);
		live.setOwnerAccount(owner);
		live.setMemberAccounts(members);
		return live;
	}

	@Override
	public Live leaveLive(String account) throws LogicException {
		String liveSid = liveDao.findLiveByMember(account);
		if (liveSid == null) {
			appendLog("不在房间中。");
			throw new LogicException("不在房间中。");
		}
		String owner = liveDao.findLiveOwner(liveSid);
		
		List<String> members = liveDao.removeLiveMember(liveSid, account);
		
		
		Live live = new Live();
		live.setSid(liveSid);
		live.setOwnerAccount(owner);
		live.setMemberAccounts(members);
		return live;
	}

	@Override
	public Live destroyLive(String account) throws LogicException {
		String liveSid = liveDao.findLiveByOwner(account);
		if (liveSid == null) {
			appendLog("不在房间中。");
			throw new LogicException("不在房间中。");
		}
		
		List<String> members = liveDao.removeLiveMembers(liveSid);
		String owner = liveDao.removeLiveOwner(liveSid);
		roomService.returnRoom(liveSid);
		
		Live live = new Live();
		live.setSid(liveSid);
		live.setOwnerAccount(owner);
		live.setMemberAccounts(members);
		return live;
	}

	@Override
	public List<String> listLiveMembersByAccount(String account) throws LogicException {
		String liveSid = liveDao.findLiveByOwner(account);
		if (liveSid == null) {
			appendLog("不在房间中。");
			throw new LogicException("不在房间中。");
		}
		List<String> members = liveDao.findLiveMembers(liveSid);
		return members;
	}

	private void checkInLive(String account) throws LogicException {
		String live = liveDao.findLiveByOwner(account);
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
			liveDao.removeLiveMember(liveSid, account);
		}
		liveSid = liveDao.findLiveByOwner(account);
		if (liveSid != null) {
			liveDao.removeLiveMembers(liveSid);
			liveDao.removeLiveOwner(liveSid);
			roomService.returnRoom(liveSid);
		}
	}


}
