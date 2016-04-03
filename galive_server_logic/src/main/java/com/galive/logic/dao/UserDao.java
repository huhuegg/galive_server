package com.galive.logic.dao;

import com.galive.logic.model.User;

public interface UserDao {

	public User findUser(String sid);
	
	public User findUserByUsername(String username);
	
	public User saveUser(User u);
}
