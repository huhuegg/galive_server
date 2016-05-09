package com.galive.logic.network.model;

import com.galive.logic.model.PlatformUser;
import com.galive.logic.model.PlatformUser.UserPlatform;
import com.galive.logic.model.User;
import com.galive.logic.model.WeChatUser;

public class RespLoginUser extends RespUser {

	public int platform;
	
	public String profile;
	
	public String wx_unionid;
	
	
	@Override
	public void convert(User u) {
		super.convert(u);
		profile = u.getProfile();
	}
	
	public void convert(PlatformUser u) {
		super.convert(u);
		platform = u.getPlatform().ordinal();
		if (platform == UserPlatform.WeChat.ordinal()) {
			WeChatUser user = (WeChatUser)u;
			wx_unionid = user.getUnionid();
		}
	}
}
