package com.galive.logic.service;

import java.util.List;

import com.galive.logic.exception.LogicException;
import com.galive.logic.model.PlatformUser;
import com.galive.logic.model.PlatformUser.UserPlatform;
import com.galive.logic.model.WeChatUser;

public interface PlatformService {
	
	public WeChatUser loginWeChat(String deviceid, String udid, String code) throws LogicException;
	
	public PlatformUser findUser(String userSid) throws LogicException;
	
	public PlatformUser findUserByDeviceid(String deviceid, UserPlatform platform) throws LogicException;
	
	public PlatformUser findUserByUdid(String udid, UserPlatform platform) throws LogicException;
	
	public void beContact(String deviceid, String udid, UserPlatform platform);
	
	public void saveSharedUdid(String deviceid, String sharedUdid);
	
	public List<PlatformUser> listRecentContacts(String userSid, int index, int size) throws LogicException;
}
