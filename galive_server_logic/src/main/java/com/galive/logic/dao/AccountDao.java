package com.galive.logic.dao;

import com.galive.logic.model.account.Platform;
import com.galive.logic.model.account.PlatformAccount;

public interface AccountDao {

	/**
	 * 保存登录Token
	 * @param account
	 * @param token
	 */
	public void saveToken(String account, String token);
	
	/**
	 * 查询Token
	 * @param account
	 * @return
	 */
	public String findToken(String account);
	
	/***
	 * 保存用户平台信息
	 * @param account
	 * @return PlatformAccount
	 */
	public PlatformAccount savePlatformAccount(PlatformAccount account);
	
	public PlatformAccount findPlatformAccount(Platform platform, String unionId);
	
	public PlatformAccount findPlatformAccount(String sid);
	
}
