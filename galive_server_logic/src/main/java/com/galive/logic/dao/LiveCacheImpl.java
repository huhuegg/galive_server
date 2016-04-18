package com.galive.logic.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import com.galive.logic.dao.cache.RedisManager;
import com.galive.logic.model.Live;
import redis.clients.jedis.Jedis;

public class LiveCacheImpl implements LiveCache {

	private Jedis jedis = RedisManager.getInstance().getResource();

	@Override
	protected void finalize() throws Throwable {
		RedisManager.getInstance().returnToPool(jedis);
		super.finalize();
	}

	// 直播id 自增
	private String liveSidKey() {
		return RedisManager.getInstance().keyPrefix() + "live:sid:incr";
	}

	// 直播信息 set k:[live:直播id] v:[直播信息json]
	private String liveKey(String liveSid) {
		return RedisManager.getInstance().keyPrefix() + "live:" + liveSid;
	}

	// 用户对应直播id hset k:[userSid:用户id] v:[直播Sid]
	private String liveOwnerKey() {
		return RedisManager.getInstance().keyPrefix() + "live:owner";
	}

	// 按最后直播时间排序 zadd
	private String listByLatestLiveTimeKey() {
		return RedisManager.getInstance().keyPrefix() + "live:list:latest_live_time";
	}

	/**
	 * 生成房间id
	 * 
	 * @return
	 */
	private String generateLiveSid() {
		long seq = jedis.incr(liveSidKey()) + 1;
		return seq + "";
	}

	@Override
	public Live saveLive(Live live) {
		String liveSid = live.getSid();
		if (StringUtils.isBlank(liveSid)) {
			liveSid = generateLiveSid();
		}
		live.setSid(liveSid);
		String json = JSON.toJSONString(live);
		String liveKey = liveKey(liveSid);
		jedis.set(liveKey, json);
		jedis.hset(liveOwnerKey(), live.getOwnerSid(), liveSid);
		return live;
	}
	
	@Override
	public Live findLive(String liveSid) {
		String json = jedis.get(liveKey(liveSid));
		if (!StringUtils.isBlank(json)) {
			Live live = JSON.parseObject(json, Live.class);
			return live;
		}
		return null;
	}

	@Override
	public Live findLiveByOwnerSid(String ownerSid) {
		String liveSid = jedis.hget(liveOwnerKey(), ownerSid);
		if (!StringUtils.isBlank(liveSid)) {
			Live live = findLive(liveSid);
			return live;
		}
		return null;
	}

	@Override
	public void insertToLiveListByLatestLiveAt(String liveSid) {
		jedis.zadd(listByLatestLiveTimeKey(), System.currentTimeMillis(), liveSid);
	}

	@Override
	public List<Live> listByLatestLiveTime(int start, int end) {
		List<Live> lives = new ArrayList<>();
		String key = listByLatestLiveTimeKey();
		Set<String> sets = jedis.zrevrange(key, start, end);
		for (String sid : sets) {
			Live live = findLive(sid);
			if (live != null) {
				lives.add(live);
			}
		}
		return lives;
	}

	

}
