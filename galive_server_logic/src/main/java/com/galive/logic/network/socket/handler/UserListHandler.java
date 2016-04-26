package com.galive.logic.network.socket.handler;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.PageParams;
import com.galive.common.protocol.PageCommandOut;
import com.galive.logic.model.Room;
import com.galive.logic.model.User;
import com.galive.logic.network.model.RespUser;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.RoomService;
import com.galive.logic.service.RoomServiceImpl;
import com.galive.logic.service.UserService;
import com.galive.logic.service.UserServiceImpl;

@SocketRequestHandler(desc = "获取用户列表", command = Command.USR_LIST)
public class UserListHandler extends SocketBaseHandler  {

	enum UserListType {
		LatestLogin
	}
	
	private UserService userService = new UserServiceImpl();
	private RoomService roomService = new RoomServiceImpl();
	
	@Override
	public String handle(String userSid, String reqData) throws Exception {
		appendLog("--UserListHandler(获取用户列表)--");
		UserListParams in = JSON.parseObject(reqData, UserListParams.class);
		
		int index = in.index;
		int size = in.size; 
		appendLog("起始游标(index):" + index);
		appendLog("分页数量(size):" + size);
		
		List<User> users = new ArrayList<>();
		if (in.type == UserListType.LatestLogin.ordinal()) {
			users = userService.listByLatestLogin(index, size);
		}

		List<RespUser> respUsers = new ArrayList<>();
		for (User u : users) {
			if (u.getSid().equals(userSid)) {
				// 过滤自己
				continue;
			}
			RespUser ru = new RespUser();
			ru.convert(u);
			Room room = roomService.findRoomByUser(u.getSid());
			if (room != null) {
				ru.roomSid = room.getSid();
				ru.invite = false;
			}
			Room inviteeRoom = roomService.findRoomByInvitee(u.getSid());
			if (inviteeRoom != null) {
				ru.invite = false;
			}
			respUsers.add(ru);
		}
		PageCommandOut<RespUser> out = new PageCommandOut<>(Command.USR_LIST, in);
		out.setData(respUsers);
		String resp = out.socketResp();
		return resp;
	}
	
	public static class UserListParams extends PageParams {
		public int type;
	}
}
