package com.galive.logic.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.galive.logic.dao.cache.RedisManager;
import redis.clients.jedis.Jedis;

public class LiveDaoImpl implements LiveDao {

	private Jedis jedis = RedisManager.getInstance().getResource();
	private int EXPIRE_SEC = 3600;

	@Override
	protected void finalize() throws Throwable {
		RedisManager.getInstance().returnToPool(jedis);
		super.finalize();
	}
	
	private String liveBelongKey(String live) {
		return RedisManager.getInstance().keyPrefix() + "live:belong:" + live;
	}

	private String liveOwnerKey(String owner) {
		return RedisManager.getInstance().keyPrefix() + "live:owner:" + owner;
	}
	
	private String liveMembersKey(String liveSid) {
		return RedisManager.getInstance().keyPrefix() + "live:members:" + liveSid;
	}
	
	private String liveMemberKey(String member) {
		return RedisManager.getInstance().keyPrefix() + "live:member" + member;
	}

	@Override
	public void saveLiveOwner(String liveSid, String account) {
		jedis.set(liveBelongKey(liveSid), account);
		jedis.expire(liveBelongKey(liveSid), EXPIRE_SEC);
		jedis.set(liveOwnerKey(account), liveSid);
		jedis.expire(liveOwnerKey(account), EXPIRE_SEC);
	}

	@Override
	public void saveLiveMember(String liveSid, String account) {
		jedis.sadd(liveMembersKey(liveSid), account);
		jedis.set(liveMemberKey(account), liveSid);
	}

	@Override
	public String findLive(String liveSid) {
		if (jedis.exists(liveBelongKey(liveSid))) {
			return liveSid;
		}
		return null;
	}

	@Override
	public String findLiveOwner(String liveSid) {
		String owner = jedis.get(liveBelongKey(liveSid));
		return owner;
	}

	@Override
	public String findLiveByOwner(String account) {
		String live = jedis.get(liveOwnerKey(account));
		return live;
	}

	@Override
	public String findLiveByMember(String account) {
		String live = jedis.get(liveMemberKey(account));
		return live;
	}

	@Override
	public List<String> findLiveMembers(String liveSid) {
		List<String> result = new ArrayList<>();
		Set<String> members = jedis.smembers(liveMembersKey(liveSid));
		for (String s : members) {
			result.add(s);
		}
		return result;
	}

	@Override
	public List<String> removeLiveMember(String liveSid, String account) {
		jedis.srem(liveMembersKey(liveSid), account);
		jedis.del(liveMemberKey(account));
		List<String> result = findLiveMembers(liveSid);
		return result;
	}

	@Override
	public List<String> removeLiveMembers(String liveSid) {
		List<String> result = findLiveMembers(liveSid);
		String key = liveMembersKey(liveSid);
		for (String s : result) {
			jedis.srem(key, s);
			jedis.del(liveMemberKey(s));
		}
		return result;
	}

	@Override
	public String removeLiveOwner(String liveSid) {
		String owner = findLiveOwner(liveSid);
		jedis.del(liveBelongKey(liveSid));
		jedis.del(liveOwnerKey(owner));
		return owner;
	}


}
