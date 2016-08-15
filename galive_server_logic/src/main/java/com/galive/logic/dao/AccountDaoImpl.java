package com.galive.logic.dao;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
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
		return RedisManager.getInstance().keyPrefix() + "account_token:" + account;
	}
	
	private String accountKey(String account) {
		return RedisManager.getInstance().keyPrefix() + "account:" + account;
	}

	@Override
	public void saveToken(String account, String token) {
		String key = tokenKey(account);
		jedis.set(key, token);
		jedis.expire(key, ApplicationConfig.getInstance().getSocketConfig().getTokenExpire());
	}

	@Override
	public String findToken(String account) {
		String token = jedis.get(tokenKey(account));
		return token;
	}

	@Override
	public Account save(Account account) {
		String act = JSON.toJSONString(account);
		jedis.set(accountKey(account.getAccount()), act);
		return account;
	}

	@Override
	public Account findAccount(String account) {
		String act = jedis.get(accountKey(account));
		if (!StringUtils.isEmpty(act)) {
			Account a = JSON.parseObject(act, Account.class);
			return a;
		}
		return null;
	}

}
