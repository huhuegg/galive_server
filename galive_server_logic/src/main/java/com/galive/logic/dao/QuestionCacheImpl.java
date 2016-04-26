package com.galive.logic.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.galive.logic.dao.cache.RedisManager;
import redis.clients.jedis.Jedis;

public class QuestionCacheImpl implements QuestionCache {

	private Jedis jedis = RedisManager.getInstance().getResource();

	@Override
	protected void finalize() throws Throwable {
		RedisManager.getInstance().returnToPool(jedis);
		super.finalize();
	}
	
	private String tagKey() {
		return RedisManager.getInstance().keyPrefix() + "question:tags";
	}

	@Override
	public void saveTag(String tag) {
		jedis.zadd(tagKey(), System.currentTimeMillis(), tag);
		
	}

	@Override
	public void deleteTag(String tag) {
		jedis.zrem(tagKey(), tag);
	}

	@Override
	public List<String> listTags() {
		Set<String> tags = jedis.zrevrange(tagKey(), 0, -1);
		List<String> result = new ArrayList<>();
		for (String tag : tags) {
			result.add(tag);
		}
		return result;
	}

}
