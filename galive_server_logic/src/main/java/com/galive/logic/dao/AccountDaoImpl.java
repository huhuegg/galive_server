package com.galive.logic.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.mongodb.morphia.query.Query;
import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.dao.cache.RedisManager;
import com.galive.logic.dao.db.MongoDao;
import com.galive.logic.dao.db.MongoManager;
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

//	@Override
//	public PlatformAccount savePlatformAccount(PlatformAccount account) {
//		if (account.getSid().isEmpty()) {
//			String sid = Sid.getNextSequence(EntitySeq.PlatformAccount) + "";	
//			account.setSid(sid);
//		}
//		platformAccountDao.save(account);
//		return account;
//	}

//	@Override
//	public PlatformAccount findPlatformAccount(Platform platform, String unionId) {
//		Query<PlatformAccount> q = platformAccountDao.createQuery();
//		q.field("platform").equal(platform);
//		String unionIdField = null;
//		switch (platform) {
//		case WeChat:
//			unionIdField = "unionid";
//			break;
//		}
//		q.field(unionIdField).equal(unionId);
//		PlatformAccount account = platformAccountDao.findOne(q);
//		return account;
//	}
//
//	@Override
//	public PlatformAccount findPlatformAccount(String sid) {
//		Query<PlatformAccount> q = platformAccountDao.createQuery();
//		q.field("sid").equal(sid);
//		PlatformAccount account = platformAccountDao.findOne(q);
//		return account;
//	}

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
	public Account saveOrUpdateAccount(Account account) {
		if (StringUtils.isEmpty(account.getSid())) {
			String sid = String.valueOf(Sid.getNextSequence(EntitySeq.Account));
			account.setSid(sid);
		} 
		dao.save(account);
		return account;
	}

	@Override
	public PlatformAccount findPlatformAccount(Platform platform, String platformUnionId) {
		Query<PlatformAccount> q = platformDao.createQuery();
		q.field("platform").equal(platform);
		q.disableValidation();
		switch (platform) {
		case Guest:
			q.field("sid").equal(platformUnionId);
			break;
		case WeChat:
			q.field("unionid").equal(platformUnionId);
			break;
		}
		PlatformAccount account = platformDao.findOne(q);
		//Account account = dao.findOne(q);
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
	public PlatformAccount saveOrUpdatePlatformAccount(PlatformAccount account) {
		if (StringUtils.isEmpty(account.getSid())) {
			String sid = String.valueOf(Sid.getNextSequence(EntitySeq.PlatformAccount));
			account.setSid(sid);
		} 
		platformDao.save(account);
		return account;
	}

	@Override
	public PlatformAccount findPlatformAccount(String platfromAccountSid) {
		Query<PlatformAccount> q = platformDao.createQuery();
		q.field("sid").equal(platfromAccountSid);
		PlatformAccount account = platformDao.findOne(q);
		return account;
	}

	

}
