package com.galive.logic.service;

import java.util.ArrayList;
import java.util.List;

import com.galive.logic.dao.LiveCache;
import com.galive.logic.dao.LiveCacheImpl;
import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Live;
import com.galive.logic.model.Live.LiveState;
import com.galive.logic.model.User;

public class LiveServiceImpl extends BaseService implements LiveService {

	private LiveCache liveCache = new LiveCacheImpl();
	private UserService userService = new UserServiceImpl();
	
	public LiveServiceImpl() {
		super();
		appendLog("AnswerServiceImpl");
	}
	
	@Override
	public Live findLive(String liveSid) throws LogicException {
		Live live = liveCache.findLive(liveSid);
		if (live == null) {
			throw new LogicException("直播不存在");
		}
		return live;
	}
	
	@Override
	public Live findLiveByUser(String userSid) {
		Live live = liveCache.findLiveByOwnerSid(userSid);
		return live;
	}
	
	@Override
	public Live findLiveByAudience(String userSid) {
		Live live = liveCache.findLiveByAudienceSid(userSid);
		return live;
	}


	@Override
	public Live startLive(String userSid) throws LogicException {
		User u = userService.findUserBySid(userSid);
		if (u == null) {
			appendLog("用户不存在。");
			throw new LogicException("用户不存在。");
		}
		Live live = liveCache.findLiveByOwnerSid(userSid);
		long now = System.currentTimeMillis();
		if (live == null) {
			live = new Live();
			live.setCreateAt(now);
			live.setName(u.getNickname() + "的直播间");
			live.setOwnerSid(userSid);
			appendLog("创建直播间" + live.desc());
		} else {
			appendLog("发现直播间" + live.desc());
		}
		live.setState(LiveState.On);
		live.setLatestLiveAt(now);
		liveCache.saveLive(live);
		
		liveCache.clearLikeNum(live.getSid());
		
		appendLog(u.desc() + "插入观众列表");
		liveCache.saveAudience(live.getSid(), userSid, true);
		appendLog(live.desc() + "按最后直播开始时间排序，插入直播列表。");
		liveCache.insertToLiveListByLatestLiveAt(live.getSid());
		return live;
	}
	
	@Override
	public Live stopLive(String userSid, String actionRecordUrl) throws LogicException {
		Live live = liveCache.findLiveByOwnerSid(userSid);
		if (live == null) {
			return null;
		}
		live.setActionRecordUrl(actionRecordUrl);
		live.setState(LiveState.Off);
		live.setLatestLiveAt(0);
		liveCache.saveLive(live);
		String liveSid = live.getSid();
		liveCache.clearLikeNum(liveSid);
		//liveCache.removeAllAudiences(liveSid);
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
//			if (live.getState() == LiveState.Off) {
//				throw new LogicException("该用户当前不在直播。");
//			}
			List<String> audienceSids = liveCache.listAudiences(liveSid, 0, -1);
			if (audienceSids.size() >= Live.MAX_AUDIENCE) {
				appendLog("超过房间上限。");
				throw new LogicException("直播间人数已满。");
			}
			
			liveCache.saveAudience(liveSid, userSid, false);
			return live;
		}
		return null;
	}

	@Override
	public Live leaveLive(String userSid) throws LogicException {
		Live live = liveCache.findLiveByAudienceSid(userSid);
		if (live == null) {
			appendLog("直播不存在或未开始。");
			return null;
		}
		if (live.getOwnerSid().equals(userSid)) {
			appendLog("主播不能离开直播间。");
			//throw new LogicException("主播不能离开直播间。");
			return null;
		}
		liveCache.removeAudience(userSid);
		return live;
	}

	@Override
	public List<User> listAudiences(String liveSid, int index, int size) throws LogicException {
		List<User> audiences = new ArrayList<>();
		List<String> audienceSids = liveCache.listAudiences(liveSid, index, index + size - 1);
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
		List<String> audienceSids = liveCache.listAudiences(liveSid, 0, -1);
		appendLog("查询所有观众:" + audienceSids.size());
		return audienceSids;
	}

	@Override
	public long[] doLike(String liveSid, String userSid) throws LogicException {
		long latestLikeTime = liveCache.latestLikeTime(liveSid, userSid);
		if (System.currentTimeMillis() - latestLikeTime <= 1000) {
			appendLog("点赞过于频繁。");
			throw new LogicException("点赞过于频繁。");
		}
		long[] likeNums = liveCache.incrLike(liveSid, userSid);
		return likeNums;
	}

	@Override
	public long[] likeNums(String liveSid) throws LogicException {
		long[] likeNums = liveCache.likeNum(liveSid);
		return likeNums;
	}

	
	
	

	
	
	

}
