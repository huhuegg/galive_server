package com.galive.logic.dao;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.mongodb.morphia.query.Query;
import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.db.MongoDao;
import com.galive.logic.db.MongoManager;
import com.galive.logic.db.RedisManager;
import com.galive.logic.model.Sid;
import com.galive.logic.model.Sid.EntitySeq;
import com.galive.logic.model.account.Account;
import com.galive.logic.model.account.Platform;
import com.galive.logic.model.account.PlatformAccount;

public class AccountDaoImpl extends BaseDao implements AccountDao {

	private MongoDao<Account> dao = new MongoDao<Account>(Account.class, MongoManager.store);
	private MongoDao<PlatformAccount> platformDao = new MongoDao<PlatformAccount>(PlatformAccount.class, MongoManager.store);

	private String tokenKey(String accountSid) {
		return RedisManager.getInstance().keyPrefix() + "account:token:" + accountSid;
	}
	
	@Override
	public void saveToken(String accountSid, String token) {
		String key = tokenKey(accountSid);
		jedis().set(key, token);
		jedis().expire(key, ApplicationConfig.getInstance().getSocketConfig().getTokenExpire());
	}

	@Override
	public String findToken(String accountSid) {
		String token = jedis().get(tokenKey(accountSid));
		return token;
	}

	@Override
	public Account findAccount(String accountSid) {
		Query<Account> q = dao.createQuery();
		q.field("sid").equal(accountSid);
		Account account = dao.findOne(q);
		return account;
	}

	@Override
	public void deleteToken(String accountSid) {
		String key = tokenKey(accountSid);
		jedis().del(key);
		
	}

	@Override
	public Account saveOrUpdate(Account account) {
		if (StringUtils.isEmpty(account.getSid())) {
			String sid = String.valueOf(Sid.getNextSequence(EntitySeq.Account));
			account.setSid(sid);
		} 
		dao.save(account);
		return account;
	}

	@Override
	public List<PlatformAccount> listPlatformAccounts(String accountSid) {
		Query<PlatformAccount> q = platformDao.createQuery();
		q.field("accountSid").equal(accountSid);
		List<PlatformAccount> accounts = platformDao.find(q).asList();
		return accounts;
	}

	@Override
	public PlatformAccount saveOrUpdate(PlatformAccount account) {
		if (StringUtils.isEmpty(account.getSid())) {
			String sid = String.valueOf(Sid.getNextSequence(EntitySeq.PlatformAccount));
			account.setSid(sid);
		} 
		platformDao.save(account);
		return account;
	}

	@Override
	public PlatformAccount findPlatformAccount(Platform platform, String platformUnionid) {
		Query<PlatformAccount> q = platformDao.createQuery();
		q.field("platform").equal(platform);
		q.field("platformUnionid").equal(platformUnionid);
		return platformDao.findOne(q);
	}}
