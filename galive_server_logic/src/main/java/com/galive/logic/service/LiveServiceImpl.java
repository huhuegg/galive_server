package com.galive.logic.service;

import java.util.List;
import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.dao.LiveCache;
import com.galive.logic.dao.LiveCacheImpl;
import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Live;

public class LiveServiceImpl extends BaseService implements LiveService {

	private LiveCache liveCache = new LiveCacheImpl();
	private RoomService roomService = new RoomServiceImpl();

	public LiveServiceImpl() {
		super();
		appendLog("LiveServiceImpl");
	}

	@Override
	public Live createLive(String account) throws LogicException {
		checkInLive(account);
		
		String liveSid = roomService.getFreeRoom();
		liveCache.saveLiveOwner(liveSid, account);
		
		Live live = new Live();
		live.setSid(liveSid);
		live.setOwnerAccount(account);
		
		return live;
	}

	@Override
	public Live joinLive(String account, String liveSid) throws LogicException {
		checkInLive(account);
		
		String owner = liveCache.findLiveOwner(liveSid);
		if (owner == null) {
			appendLog("房间不存在");
			throw new LogicException("房间不存在。");
		}
		
		List<String> members = liveCache.findLiveMembers(liveSid);
		if (members.size() >= ApplicationConfig.getInstance().getLogicConfig().getMaxLiveMember()) {
			throw new LogicException("该房间已满员。");
		}
		liveCache.saveLiveMember(liveSid, account);
		members.add(account);
		Live live = new Live();
		live.setSid(liveSid);
		live.setOwnerAccount(account);
		live.setMemberAccounts(members);
		return live;
	}

	@Override
	public Live leaveLive(String account) throws LogicException {
		String liveSid = liveCache.findLiveByMember(account);
		if (liveSid == null) {
			appendLog("不在房间中。");
			throw new LogicException("不在房间中。");
		}
		String owner = liveCache.findLiveOwner(liveSid);
		
		List<String> members = liveCache.removeLiveMember(liveSid, account);
		
		
		Live live = new Live();
		live.setSid(liveSid);
		live.setOwnerAccount(owner);
		live.setMemberAccounts(members);
		return live;
	}

	@Override
	public Live destroyLive(String account) throws LogicException {
		String liveSid = liveCache.findLiveByOwner(account);
		if (liveSid == null) {
			appendLog("不在房间中。");
			throw new LogicException("不在房间中。");
		}
		
		List<String> members = liveCache.removeLiveMembers(liveSid);
		String owner = liveCache.removeLiveOwner(liveSid);
		roomService.returnRoom(liveSid);
		
		Live live = new Live();
		live.setSid(liveSid);
		live.setOwnerAccount(owner);
		live.setMemberAccounts(members);
		return live;
	}

	@Override
	public List<String> listLiveMembersByAccount(String account) throws LogicException {
		String liveSid = liveCache.findLiveByOwner(account);
		if (liveSid == null) {
			appendLog("不在房间中。");
			throw new LogicException("不在房间中。");
		}
		List<String> members = liveCache.findLiveMembers(liveSid);
		return members;
	}

	private void checkInLive(String account) throws LogicException {
		String live = liveCache.findLiveByOwner(account);
		if (live == null) {
			live = liveCache.findLiveByMember(account);
		}
		if (live != null) {
			throw new LogicException("已在房间中。");
		}
	}


}
