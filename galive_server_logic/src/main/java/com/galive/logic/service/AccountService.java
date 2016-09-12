package com.galive.logic.service;

import com.galive.logic.exception.LogicException;
import com.galive.logic.model.PlatformAccount;
import com.galive.logic.model.PlatformAccountWeChat;

public interface AccountService {

	public String generateToken(String sid);
	
	public boolean verifyToken(String account, String token);
	
	public boolean verifyAccount(String account);
	
	public PlatformAccount getPlatformAccountInfo(String sid) throws LogicException;
	
	public PlatformAccountWeChat loginWechat(String code) throws LogicException;
	
}
