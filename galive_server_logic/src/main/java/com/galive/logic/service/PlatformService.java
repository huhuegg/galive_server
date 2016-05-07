package com.galive.logic.service;

import com.galive.logic.exception.LogicException;
import com.galive.logic.model.PlatformUser;
import com.galive.logic.model.PlatformUser.UserPlatform;
import com.galive.logic.model.WeChatUser;

public interface PlatformService {
	
	public WeChatUser loginWeChat(String udid, String code) throws LogicException;
	
	public PlatformUser findUser(String udid, UserPlatform platform) throws LogicException;
}
