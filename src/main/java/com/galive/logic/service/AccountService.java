package com.galive.logic.service;

import com.galive.logic.exception.LogicException;
import com.galive.logic.model.account.Account;
import com.galive.logic.model.account.Platform;

public interface AccountService {

	/**
	 * 根据sid查找用户
	 * @param accountSid 账户id
	 * @return 账户
	 */
	Account findAndCheckAccount(String accountSid) throws LogicException;
	
	/**
	 * 更新用户信息
	 * @param act
	 * @throws LogicException
	 */
	void updateAccount(Account act) throws LogicException;
	
	/**
	 * 生成token
	 * @param accountSid
	 * @return
	 */
	String generateToken(String accountSid);
	
	/**
	 * 验证token是否有效
	 * @param accountSid
	 * @param token
	 * @return
	 */
	boolean verifyToken(String accountSid, String token);
	
	/**
	 * 验证用户是否有效
	 * @param accountSid
	 * @return
	 */
	boolean verifyAccount(String accountSid);
	
	/**
	 * 登录
	 * @param accountSid 账号id
	 * @param platform 登录平台
	 * @param platformParams 三方平台所需参数 微信为code
	 * @return
	 * @throws LogicException
	 */
	Account login(String accountSid, Platform platform, String platformParams) throws LogicException;
	
	/**
	 * 登出
	 * @param accountSid
	 * @throws LogicException
	 */
	void logout(String accountSid) throws LogicException;
	
}
