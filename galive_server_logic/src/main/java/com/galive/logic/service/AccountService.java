package com.galive.logic.service;

public interface AccountService {

	public boolean verifyToken(String accountSid, String token);
	
	public boolean verifyAccount(String account);
}
