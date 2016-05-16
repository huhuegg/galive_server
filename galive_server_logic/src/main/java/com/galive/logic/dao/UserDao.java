package com.galive.logic.dao;

import java.util.List;

import com.galive.logic.model.User;

public interface UserDao {

	public User find(String sid);
	
	public User findByDeviceid(String deviceid);
	
	public User findByUsername(String username);
	
	public User saveOrUpdate(User u);
	
	public User findWXUserByUnionid(String unionid);
	
	public List<User> findUserByDeviceid(String deviceid);
}
