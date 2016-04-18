package com.galive.logic.service;

import java.util.List;

import com.galive.logic.dao.LiveCache;
import com.galive.logic.dao.LiveCacheImpl;
import com.galive.logic.exception.LogicException;
import com.galive.logic.helper.LoggerHelper;
import com.galive.logic.model.Live;
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
		// 更新最后直播时间
		live.setLatestLiveAt(now);
		liveCache.saveLive(live);
		
		// 插入排序
		liveCache.insertToLiveListByLatestLiveAt(live.getSid());
		return live;
	}

	@Override
	public void stopLive() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Live> listByLatestLiveTime(int index, int size) {
		List<Live> lives = liveCache.listByLatestLiveTime(index, index + size - 1);
		return lives;
	}
	
	

}
