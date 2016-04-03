package com.galive.logic.network.model;

import com.galive.logic.model.User;
import com.galive.logic.network.socket.ChannelManager;

public class RespUser {

	public String sid = "";
	
	public String avatar = "";
	
	public String nickname = "";
	
	public String roomSid = "";
	
	public int onlineState;
	
	public static RespUser convert(User u) {
		RespUser user = new RespUser();
		user.sid = u.getSid();
		user.nickname = u.getNickname();
		user.avatar = u.getAvatar();
		user.onlineState = ChannelManager.getInstance().onlineState(user.sid).ordinal();
		return user;
	}

}
