package com.galive.logic.service;

import java.util.Map;

import com.galive.logic.exception.LogicException;
import com.galive.logic.model.MeetingMemberOptions;
import com.galive.logic.model.MeetingOptions;
import com.galive.logic.model.account.Account;
import com.galive.logic.model.account.Platform;
import com.galive.logic.model.account.PlatformAccount;

public interface AccountService {

	/**
	 * 生成token
	 * @param accountSid
	 * @return
	 */
	public String generateToken(String accountSid);
	
	/**
	 * 验证token是否有效
	 * @param accountSid
	 * @param token
	 * @return
	 */
	public boolean verifyToken(String accountSid, String token);
	
	/**
	 * 验证用户是否有效
	 * @param accountSid
	 * @return
	 */
	public boolean verifyAccount(String accountSid);
	
	/**
	 * 根据sid查找用户
	 * @param accountSid
	 * @return
	 */
	public Account findAndCheckAccount(String accountSid) throws LogicException;
	
	public PlatformAccount findPlatformAccount(String platformAccountSid) throws LogicException;
	

	public MeetingOptions updateMeetingOptions(String accountSid, MeetingOptions options) throws LogicException;
	
	public MeetingMemberOptions updateMeetingMemberOptions(String accountSid, MeetingMemberOptions options) throws LogicException;
	
	/**
	 * 登录
	 * @param accountSid 账号id 如果为空，生成对应Account，否则与现有Account绑定
	 * @param platform 登录平台
	 * @param params 登录平台所需参数
	 * @return
	 * @throws LogicException
	 */
	public PlatformAccount login(String accountSid, Platform platform, Map<String, Object> params) throws LogicException;
	
	/**
	 * 登出
	 * @param accountSid
	 * @throws LogicException
	 */
	public void logout(String accountSid) throws LogicException;
	

}
