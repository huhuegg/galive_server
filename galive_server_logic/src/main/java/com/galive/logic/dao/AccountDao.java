package com.galive.logic.dao;

import com.galive.logic.model.Account;

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
	
	public Account save(Account account);
	
	public Account findAccount(String account);
	
}
