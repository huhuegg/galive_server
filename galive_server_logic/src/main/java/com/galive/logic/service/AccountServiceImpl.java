package com.galive.logic.service;

import java.util.UUID;
import org.apache.commons.codec.digest.Md5Crypt;
import com.galive.logic.dao.AccountDao;
import com.galive.logic.dao.AccountDaoImpl;
import com.galive.logic.model.Account;

public class AccountServiceImpl extends BaseService implements AccountService {

	private AccountDao accountDao = new AccountDaoImpl();
	
	public AccountServiceImpl() {
		super();
		appendLog("AccountServiceImpl");
	}
	
	@Override
	public String generateToken(String account) {
		String uuid = UUID.randomUUID().toString();
		String token = Md5Crypt.md5Crypt(uuid.getBytes());
		accountDao.saveToken(account, token);
		return token;
	}

	@Override
	public boolean verifyToken(String account, String token) {
		String existToken = accountDao.findToken(account);
		if (existToken != null) {
			return existToken.equals(token);
		}
		return false;
	}

	@Override
	public boolean verifyAccount(String account) {
		String existToken = accountDao.findToken(account);
		return existToken != null;
	}

	@Override
	public void saveAccount(Account account) {
		accountDao.save(account);
	}

	@Override
	public Account findAccount(String account) {
		// TODO Auto-generated method stub
		Account act = accountDao.findAccount(account);
		return act;
	}

	

	
}
