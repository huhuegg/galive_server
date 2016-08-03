package com.galive.logic.dao;

public interface AccountDao {

	/**
	 * 保存登录Token
	 * @param accountSid
	 * @param token
	 */
	public void saveToken(String accountSid, String token);
	
	/**
	 * 查询Token
	 * @param accountSid
	 * @return
	 */
	public String findToken(String accountSid);
	
}
