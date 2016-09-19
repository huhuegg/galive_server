package com.galive.logic.dao;

import java.util.List;

import com.galive.logic.model.account.Account;
import com.galive.logic.model.account.Platform;
import com.galive.logic.model.account.PlatformAccount;

public interface AccountDao {

	public void saveToken(String accountSid, String token);
	
	public void deleteToken(String accountSid);
	
	public Account saveOrUpdateAccount(Account account);

	public String findToken(String accountSid);
	
	public Account findAccount(String accountSid);
	
	public PlatformAccount findPlatformAccount(Platform platform, String platformUnionId);
	
	public List<PlatformAccount> listPlatformAccounts(String accountSid);
//
//	public PlatformAccount savePlatformAccount(PlatformAccount account);
//	
//	public PlatformAccount findPlatformAccount(Platform platform, String unionId);
//	
//	public PlatformAccount findPlatformAccount(String sid);
	
}
