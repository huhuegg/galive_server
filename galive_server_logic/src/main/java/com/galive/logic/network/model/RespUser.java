package com.galive.logic.network.model;

import com.galive.logic.model.User;
import com.galive.logic.model.User.UserGender;
import com.galive.logic.network.socket.ChannelManager;

public class RespUser {

	public String sid = "";
	
	public String avatar = "";
	
	public String nickname = "";
	
	public String roomSid = "";
	
	public String liveSid = "";
	
	public int onlineState;
	
	public boolean invite = true;
	
	public int gender = UserGender.Unknown.ordinal();
	
	public void convert(User u) {
		sid = u.getSid();
		nickname = u.getNickname();
		avatar = u.getAvatar();
		onlineState = ChannelManager.getInstance().getOnlineState(sid).ordinal();
		gender = u.getGender().ordinal();
	}


}
