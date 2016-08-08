package com.galive.logic.service;

import com.galive.logic.model.Account;

public interface AccountService {

	public String generateToken(String account);
	
	public boolean verifyToken(String account, String token);
	
	public boolean verifyAccount(String account);
	
	public void saveAccount(Account account);
}
