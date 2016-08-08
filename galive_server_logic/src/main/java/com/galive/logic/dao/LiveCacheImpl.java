package com.galive.logic.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

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

	// 观众列表 按加入时间排序 zadd
	private String listAudienceByJoinTimeKey(String liveSid) {
		return RedisManager.getInstance().keyPrefix() + "live:audience:list:join_time:" + liveSid;
	}

	// 查找用户当前观看的直播
	private String audienceKey() {
		return RedisManager.getInstance().keyPrefix() + "live:audience";
	}

	// 直播单场点赞数 hset
	private String likeKey() {
		return RedisManager.getInstance().keyPrefix() + "live:like";
	}

	// 直播点赞总数 hset
	private String likeAllKey() {
		return RedisManager.getInstance().keyPrefix() + "live:like:all";
	}

	// 最后点赞时间 hset
	private String latestLikeTimeKey(String liveSid) {
		return RedisManager.getInstance().keyPrefix() + "live:like:latest_time:" + liveSid;
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
		//jedis.hset(liveOwnerKey(), live.getOwnerSid(), liveSid);
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
	public Live findLiveByAudienceSid(String audienceSid) {
		String liveSid = jedis.hget(audienceKey(), audienceSid);
		if (!StringUtils.isEmpty(liveSid)) {
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
	public List<String> listByLatestLiveTime(int start, int end) {
		List<String> lives = new ArrayList<>();
		String key = listByLatestLiveTimeKey();
		Set<String> sets = jedis.zrevrange(key, start, end);
		for (String sid : sets) {
			lives.add(sid);
		}
		return lives;
	}

	@Override
	public void saveAudience(String liveSid, String userSid, boolean isPresenter) {
		long now = isPresenter ? 0 : System.currentTimeMillis();
		jedis.hset(audienceKey(), userSid, liveSid);
		jedis.zadd(listAudienceByJoinTimeKey(liveSid), now, userSid);
	}

	@Override
	public void removeAudience(String userSid) {
		String liveSid = jedis.hget(audienceKey(), userSid);
		if (!StringUtils.isEmpty(liveSid)) {
			jedis.zrem(listAudienceByJoinTimeKey(liveSid), userSid);
		}
		jedis.hdel(audienceKey(), userSid);
	}
	
	@Override
	public void removeAllAudiences(String liveSid) {
		List<String> audiences = listAudiences(liveSid, 0, -1);
		for (String sid : audiences) {
			jedis.hdel(audienceKey(), sid);
		}
		jedis.zremrangeByRank(listAudienceByJoinTimeKey(liveSid), 0, -1);
	}

	@Override
	public List<String> listAudiences(String liveSid, int start, int end) {
		List<String> userSids = new ArrayList<>();
		String key = listAudienceByJoinTimeKey(liveSid);
		Set<String> sets = jedis.zrevrange(key, start, end);
		for (String sid : sets) {
			userSids.add(sid);
		}
		return userSids;
	}

	@Override
	public long countAudiences(String liveSid) {
		String key = listAudienceByJoinTimeKey(liveSid);
		long count = jedis.zcard(key);
		return count;
	}

	@Override
	public long[] incrLike(String liveSid, String userSid) {
		String likeKey = likeKey();
		String likeAllKey = likeAllKey();
		long likeNum = jedis.hincrBy(likeKey, liveSid, 1);
		long likeAllNum = jedis.hincrBy(likeAllKey, liveSid, 1);
		String key = latestLikeTimeKey(liveSid);
		jedis.hset(key, userSid, System.currentTimeMillis() + "");
		return new long[] { likeNum, likeAllNum };
	}

	@Override
	public void clearLikeNum(String liveSid) {
		String likeKey = likeKey();
		jedis.hdel(likeKey, liveSid);
	}

	@Override
	public long[] likeNum(String liveSid) {
		String likeKey = likeKey();
		String likeAllKey = likeAllKey();
		long likeNum = NumberUtils.toLong(jedis.hget(likeKey, liveSid), 0);
		long likeAllNum = NumberUtils.toLong(jedis.hget(likeAllKey, liveSid), 0);
		return new long[] { likeNum, likeAllNum };
	}

	@Override
	public long latestLikeTime(String liveSid, String userSid) {
		String key = latestLikeTimeKey(liveSid);
		String timeStr = jedis.hget(key, userSid);
		long time = NumberUtils.toLong(timeStr, 0);
		return time;
	}

	@Override
	public void saveLiveOwner(String account, String liveSid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveLiveMember(String account, String liveSid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Live findLiveByOwner(String account) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Live findLiveByMember(String account) {
		// TODO Auto-generated method stub
		return null;
	}

}
