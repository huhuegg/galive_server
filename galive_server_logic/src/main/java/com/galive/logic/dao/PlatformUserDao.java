package com.galive.logic.dao;

import java.util.List;

import com.galive.logic.model.PlatformUser;
import com.galive.logic.model.PlatformUser.UserPlatform;

public interface PlatformUserDao {

	public PlatformUser find(String userSid);
	
	public PlatformUser findByDeviceid(String deviceid, UserPlatform platform);
	
	public PlatformUser findByUdid(String udid, UserPlatform platform);
	
	public PlatformUser saveOrUpdate(PlatformUser u);
	
	public List<PlatformUser> listByDeviceid(String deviceid);
}
