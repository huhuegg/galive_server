package com.galive.logic.handler.model;

import com.galive.logic.model.Room;
import com.galive.logic.model.User;

public class RespUser {

	public String sid = "";
	
	public String avatar = "";
	
	public String nickname = "";
	
	public String roomSid = "";
	
	public int onlineState;
	
	public static RespUser convertFromUser(User u) {
		RespUser user = new RespUser();
		user.sid = u.getSid();
		user.nickname = u.getNickname();
		user.avatar = u.getAvatar();
		Room room = Room.findRoomByUser(u.getSid());
		if (room != null) {
			user.roomSid = room.getRoomId();
		}
		user.onlineState = User.onlineState(u.getSid()).ordinal();
		return user;
	}

}
