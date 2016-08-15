package com.galive.logic.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.galive.logic.dao.cache.RedisManager;
import redis.clients.jedis.Jedis;

public class LiveDaoImpl implements LiveDao {

	private Jedis jedis = RedisManager.getInstance().getResource();

	@Override
	protected void finalize() throws Throwable {
		RedisManager.getInstance().returnToPool(jedis);
		super.finalize();
	}
	
	private String liveOwnerKey(String live) {
		return RedisManager.getInstance().keyPrefix() + "live_belong:" + live;
	}

	private String ownerLiveKey(String owner) {
		return RedisManager.getInstance().keyPrefix() + "live_owner:" + owner;
	}
	
	private String liveMembersKey(String liveSid) {
		return RedisManager.getInstance().keyPrefix() + "live_members:" + liveSid;
	}
	
	private String liveMemberKey(String member) {
		return RedisManager.getInstance().keyPrefix() + "live_member_belong:" + member;
	}

	@Override
	public void saveLiveOwner(String liveSid, String account) {
		jedis.set(liveOwnerKey(liveSid), account);
		jedis.set(ownerLiveKey(account), liveSid);
	}

	@Override
	public void saveLiveMember(String liveSid, String account) {
		jedis.sadd(liveMembersKey(liveSid), account);
		jedis.set(liveMemberKey(account), liveSid);
	}

	@Override
	public String findLive(String liveSid) {
		if (jedis.exists(liveOwnerKey(liveSid))) {
			return liveSid;
		}
		return null;
	}

	@Override
	public String findLiveOwner(String liveSid) {
		String owner = jedis.get(liveOwnerKey(liveSid));
		return owner;
	}

	@Override
	public String findLiveByOwner(String account) {
		String live = jedis.get(ownerLiveKey(account));
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
		jedis.del(liveOwnerKey(liveSid));
		jedis.del(ownerLiveKey(owner));
		return owner;
	}


}
