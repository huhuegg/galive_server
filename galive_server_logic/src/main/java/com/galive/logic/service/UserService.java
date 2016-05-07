package com.galive.logic.service;

import java.util.List;

import com.galive.logic.exception.LogicException;
import com.galive.logic.model.User;
import com.galive.logic.model.User.UserGender;

public interface UserService {
	
	public User register(String username, String password, String nickname, String avatar, UserGender gender, String profile) throws LogicException;
	
	public User login(String username, String password) throws LogicException;
	
	public User findUserBySid(String userSid) throws LogicException;

	public List<User> listByLatestLogin(int index, int size);
	
	public void updateDeviceToken(String userSid, String deviceToken) throws LogicException;
	
	public void deleteDeviceToken(String deviceToken) throws LogicException;
	
	public String findDeviceToken(String userSid);
	
	public boolean verifyToken(String userSid, String token);
	
	public String createToken(String userSid) throws LogicException;
	
	public boolean isOnline(String userSid);
	
}
