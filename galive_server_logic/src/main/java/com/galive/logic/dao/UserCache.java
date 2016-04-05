package com.galive.logic.dao;

import java.util.List;

import com.galive.logic.model.User;

public interface UserCache {

	public void updateLatestLogin(String userSid);
	
	public List<User> listByLatestLogin(int start, int end);
	
	public void saveDeviceToken(String userSid, String deviceToken);
	
	public void deleteDeviceToken(String deviceToken);
	
	public void deleteDeviceTokenByUserSid(String userSid);
	
	public String findDeviceToken(String userSid);
	
	public String findUserToken(String userSid);
	
	public void saveUserToken(String userSid, String token);
}
