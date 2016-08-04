package com.galive.logic.service;

public interface AccountService {

	public String generateToken(String account);
	
	public boolean verifyToken(String account, String token);
	
	public boolean verifyAccount(String account);
}
