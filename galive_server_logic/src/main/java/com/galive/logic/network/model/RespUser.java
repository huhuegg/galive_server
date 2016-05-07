package com.galive.logic.network.model;

import com.galive.logic.model.PlatformUser;
import com.galive.logic.model.User;
import com.galive.logic.network.socket.ChannelManager;

public class RespUser {

	public String sid = "";
	
	public String avatar = "";
	
	public String nickname = "";
	
	public String roomSid = "";
	
	public int gender;
	
	public int onlineState;
	
	public boolean invite = true;
	
	public void convert(User u) {
		sid = u.getSid();
		nickname = u.getNickname();
		gender = u.getGender().ordinal();
		avatar = u.getAvatar();
		onlineState = ChannelManager.getInstance().getOnlineState(sid).ordinal();
	}
	
	public void convert(PlatformUser u) {
		sid = u.getSid();
		nickname = u.getNickname();
		avatar = u.getAvatar();
	}

}
