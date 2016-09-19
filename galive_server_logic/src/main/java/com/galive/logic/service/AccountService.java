package com.galive.logic.service;

import com.galive.logic.exception.LogicException;
import com.galive.logic.model.account.Account;
import com.galive.logic.model.account.Platform;
import com.galive.logic.model.account.PlatformAccount;

public interface AccountService {

	public String generateToken(String accountSid);
	
	public boolean verifyToken(String accountSid, String token);
	
	public boolean verifyAccount(String accountSid);
	
	public Account findAccount(String accountSid);
	
	public PlatformAccount login(String accountSid, Platform platform, String params) throws LogicException;
	
	public void logout(String accountSid) throws LogicException;
	

}
