package com.galive.logic.dao;

import com.galive.logic.model.PlatformUser;
import com.galive.logic.model.PlatformUser.UserPlatform;

public interface PlatformUserDao {

	public PlatformUser find(String udid, UserPlatform platform);
	
	public PlatformUser saveOrUpdate(PlatformUser u);
}
