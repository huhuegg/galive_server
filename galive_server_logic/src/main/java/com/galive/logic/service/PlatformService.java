package com.galive.logic.service;

import com.galive.logic.exception.LogicException;
import com.galive.logic.model.PlatformUser;
import com.galive.logic.model.PlatformUser.UserPlatform;
import com.galive.logic.model.WeChatUser;

public interface PlatformService {
	
	public WeChatUser loginWeChat(String deviceid, String udid, String code) throws LogicException;
	
	public PlatformUser findUser(String deviceId, UserPlatform platform) throws LogicException;
	
	public PlatformUser findUserByUdid(String udid, UserPlatform platform) throws LogicException;
	
	public void beContact(String deviceid, String udid, UserPlatform platform);
	
	public void saveSharedUdid(String deviceid, String sharedUdid);
}
