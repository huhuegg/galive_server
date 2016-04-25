package com.galive.logic.dao;

import com.galive.logic.model.User;

public interface UserDao {

	public User find(String sid);
	
	public User findByUsername(String username);
	
	public User saveOrUpdate(User u);
}
