package com.galive.logic.network.model;

import com.galive.logic.model.User;

public class RespLoginUser extends RespUser {

	public int platform;
	
	public String profile;
	
	public String wx_unionid;
	
	
	@Override
	public void convert(User u) {
		super.convert(u);
		profile = u.getProfile();
	}
	

}
