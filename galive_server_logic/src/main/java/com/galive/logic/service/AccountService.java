package com.galive.logic.service;

public interface AccountService {

	public String generateToken(String account, String channel);
	
	public boolean verifyToken(String account, String channel, String token);
	
	public boolean verifyAccount(String account, String channel);
}
