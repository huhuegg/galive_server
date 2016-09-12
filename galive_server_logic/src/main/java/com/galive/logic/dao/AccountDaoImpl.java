package com.galive.logic.dao;

import org.mongodb.morphia.query.Query;

import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.dao.cache.RedisManager;
import com.galive.logic.dao.db.MongoDao;
import com.galive.logic.dao.db.MongoManager;
import com.galive.logic.model.Platform;
import com.galive.logic.model.PlatformAccount;
import com.galive.logic.model.Sid;
import com.galive.logic.model.Sid.EntitySeq;

import redis.clients.jedis.Jedis;

public class AccountDaoImpl implements AccountDao {

	private MongoDao<PlatformAccount> platformAccountDao = new MongoDao<PlatformAccount>(PlatformAccount.class, MongoManager.store);
	private Jedis jedis = RedisManager.getInstance().getResource();

	@Override
	protected void finalize() throws Throwable {
		RedisManager.getInstance().returnToPool(jedis);
		super.finalize();
	}

	private String tokenKey(String account) {
		return RedisManager.getInstance().keyPrefix() + "account:token:" + account;
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
	public PlatformAccount savePlatformAccount(PlatformAccount account) {
		if (account.getSid().isEmpty()) {
			String sid = Sid.getNextSequence(EntitySeq.PlatformAccount) + "";	
			account.setSid(sid);
		}
		platformAccountDao.save(account);
		return account;
	}

	@Override
	public PlatformAccount findPlatformAccount(Platform platform, String unionId) {
		Query<PlatformAccount> q = platformAccountDao.createQuery();
		q.field("platform").equal(platform);
		String unionIdField = null;
		switch (platform) {
		case WeChat:
			unionIdField = "unionid";
			break;
		}
		q.field(unionIdField).equal(unionId);
		PlatformAccount account = platformAccountDao.findOne(q);
		return account;
	}

	@Override
	public PlatformAccount findPlatformAccount(String sid) {
		Query<PlatformAccount> q = platformAccountDao.createQuery();
		q.field("sid").equal(sid);
		PlatformAccount account = platformAccountDao.findOne(q);
		return account;
	}

	

}
