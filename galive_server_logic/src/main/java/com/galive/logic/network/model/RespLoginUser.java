package com.galive.logic.network.model;

import com.galive.logic.model.User;

public class RespLoginUser extends RespUser {

	public String profile;
	
	@Override
	public void convert(User u) {
		super.convert(u);
		profile = u.getProfile();
	}
}
