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

	private String liveCreatorKey(String liveSid) {
		return RedisManager.getInstance().keyPrefix() + "live:creator:" + liveSid;
	}

	private String liveByCreatorKey(String creator) {
		return RedisManager.getInstance().keyPrefix() + "live:by_creator:" + creator;
	}
	
	private String liveMembersKey(String liveSid) {
		return RedisManager.getInstance().keyPrefix() + "live:members:" + liveSid;
	}
	
	private String liveMemberKey(String member) {
		return RedisManager.getInstance().keyPrefix() + "live:member:" + member;
	}
	
	@Override
	public void saveLiveCreator(String liveSid, String account) {
		String key = liveCreatorKey(liveSid);
		jedis.set(key, account);
	}
	
	@Override
	public void saveLiveForCreator(String liveSid, String account) {
		String key = liveByCreatorKey(account);
		jedis.set(key, liveSid);
	}

	@Override
	public String findLiveCreator(String liveSid) {
		String key = liveCreatorKey(liveSid);
		String creator = jedis.get(key);
		return creator;
	}

	@Override
	public String findLiveByCreator(String account) {
		String key = liveByCreatorKey(account);
		String live = jedis.get(key);
		return live;
	}

	@Override
	public String removeLiveCreator(String liveSid) {
		String key = liveCreatorKey(liveSid);
		jedis.del(key);
		return liveSid;
	}
	
	@Override
	public String removeLiveForCreator(String creator) {
		String key = liveByCreatorKey(creator);
		jedis.del(key);
		return creator;
	}
	
	@Override
	public void saveLiveMember(String liveSid, String account) {
		String key = liveMembersKey(liveSid);
		jedis.sadd(key, account);
		
	}
	
	@Override
	public void saveLiveForMember(String liveSid, String account) {
		jedis.set(liveMemberKey(account), liveSid);
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
		List<String> result = findLiveMembers(liveSid);
		return result;
	}
	
	@Override
	public void removeLiveForMember(String liveSid, String account) {
		jedis.del(liveMemberKey(account));
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
	
	public boolean liveExsit(String liveSid) {
		if (jedis.exists(liveCreatorKey(liveSid))) {
			return true;
		}
		return false;
	}

	

	

	

	


}
