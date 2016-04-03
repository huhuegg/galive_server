package com.galive.logic.service;

import java.util.List;

import com.galive.logic.exception.LogicException;
import com.galive.logic.model.User;

public interface UserService {
	
	public User register(String username, String password, String nickname) throws LogicException;
	
	public User login(String username, String password) throws LogicException;
	
	public User findUserBySid(String userSid) throws LogicException;

	public List<User> listByLatestLogin(int index, int size);
	
	public void updateUserDeviceToken(String userSid, String deviceToken) throws LogicException;
	
	public boolean verifyToken(String userSid, String token);
	
	public String createToken(String userSid) throws LogicException;
}
