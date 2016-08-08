package com.galive.logic.dao;

import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.dao.cache.RedisManager;
import com.galive.logic.model.Account;

import redis.clients.jedis.Jedis;

public class AccountDaoImpl implements AccountDao {

	private Jedis jedis = RedisManager.getInstance().getResource();

	@Override
	protected void finalize() throws Throwable {
		RedisManager.getInstance().returnToPool(jedis);
		super.finalize();
	}

	private String tokenKey(String account) {
		return RedisManager.getInstance().keyPrefix() + "live:token:" + account;
	}

	@Override
	public void saveToken(String account, String token) {
		String key = tokenKey(account);
		jedis.set(key, token);
		jedis.expire(key, ApplicationConfig.getInstance().getSocketConfig().getTokenExpire());
	}

	@Override
	public String findToken(String account) {
		// TODO 查询token
		return null;
	}

	@Override
	public Account save(Account account) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Account findAccount(String account) {
		// TODO Auto-generated method stub
		return null;
	}

}
