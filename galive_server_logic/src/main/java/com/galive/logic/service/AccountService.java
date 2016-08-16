package com.galive.logic.service;

import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Account;
import com.galive.logic.network.platform.wx.WXUserInfoResp;

public interface AccountService {

	public String generateToken(String account);
	
	public boolean verifyToken(String account, String token);
	
	public boolean verifyAccount(String account);
	
	public void saveAccount(Account account);
	
	public Account findAccount(String account);
	
	public WXUserInfoResp reqWechatUserInfo(String code) throws LogicException;
}
