package com.galive.logic.service;

import java.util.ArrayList;
import java.util.List;

import com.galive.logic.dao.LiveCache;
import com.galive.logic.dao.LiveCacheImpl;
import com.galive.logic.exception.LogicException;
import com.galive.logic.helper.LoggerHelper;
import com.galive.logic.model.Live;
import com.galive.logic.model.Live.LiveState;
import com.galive.logic.model.User;

public class LiveServiceImpl implements LiveService {

	private StringBuffer logBuffer;
	private LiveCache liveCache;
	private UserService userService;
	
	public LiveServiceImpl(StringBuffer logBuffer) {
		this.logBuffer = logBuffer;
		liveCache = new LiveCacheImpl();
		userService = new UserServiceImpl(logBuffer);
	}
	
	@Override
	public Live findLiveByUser(String userSid) {
		Live live = liveCache.findLiveByOwnerSid(userSid);
		return live;
	}

	@Override
	public Live startLive(String userSid) throws LogicException {
		Live live = liveCache.findLiveByOwnerSid(userSid);
		long now = System.currentTimeMillis();
		if (live == null) {
			live = new Live();
			live.setCreateAt(now);
			User u = userService.findUserBySid(userSid);
			if (u == null) {
				LoggerHelper.appendLog("用户不存在。", logBuffer);
				throw new LogicException("用户不存在。");
			}
			live.setName(u.getNickname() + "的直播间");
			live.setOwnerSid(userSid);
		}
		live.setState(LiveState.On);
		live.setLatestLiveAt(now);
		liveCache.saveLive(live);
		
		// 插入排序
		liveCache.insertToLiveListByLatestLiveAt(live.getSid());
		return live;
	}
	
	@Override
	public Live stopLive(String userSid) throws LogicException {
		Live live = liveCache.findLiveByOwnerSid(userSid);
		if (live == null) {
			return null;
		}
		live.setState(LiveState.Off);
		live.setLatestLiveAt(0);
		liveCache.saveLive(live);
		return live;
	}

	@Override
	public List<Live> listByLatestLiveTime(int index, int size) {
		List<Live> lives = new ArrayList<>();
		List<String> liveSids = liveCache.listByLatestLiveTime(index, index + size - 1);
		for (String sid : liveSids) {
			Live live = liveCache.findLive(sid);
			if (live != null) {
				lives.add(live);
			}
		}
		return lives;
	}

	@Override
	public Live joinLive(String liveSid, String userSid) throws LogicException {
		Live live = liveCache.findLive(liveSid);
		if (live != null) {
			if (live.getState() == LiveState.Off) {
				throw new LogicException("该用户当前不在直播。");
			}
			liveCache.saveAudience(liveSid, userSid);
			return live;
		}
		return null;
	}

	@Override
	public Live leaveLive(String userSid) throws LogicException {
		Live live = liveCache.findLiveByAudienceSid(userSid);
		liveCache.removeAudience(userSid);
		return live;
	}

	@Override
	public List<User> listAudiences(String liveSid, int index, int size) throws LogicException {
		List<User> audiences = new ArrayList<>();
		List<String> audienceSids = liveCache.listAudience(liveSid, index, index + size - 1);
		for (String sid : audienceSids) {
			User u = userService.findUserBySid(sid);
			if (u != null) {
				audiences.add(u);
			}
		}
		return audiences;
	}

	@Override
	public List<String> listAllAudiences(String liveSid) throws LogicException {
		List<String> audienceSids = liveCache.listAudience(liveSid, 0, -1);
		return audienceSids;
	}

	

	
	
	

}
