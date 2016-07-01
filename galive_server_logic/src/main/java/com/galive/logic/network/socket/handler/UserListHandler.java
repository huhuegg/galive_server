package com.galive.logic.network.socket.handler;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.common.protocol.PageCommandIn;
import com.galive.common.protocol.PageCommandOut;
import com.galive.logic.model.Live;
import com.galive.logic.model.Room;
import com.galive.logic.model.User;
import com.galive.logic.network.model.RespUser;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.LiveService;
import com.galive.logic.service.LiveServiceImpl;
import com.galive.logic.service.RoomService;
import com.galive.logic.service.RoomServiceImpl;
import com.galive.logic.service.UserService;
import com.galive.logic.service.UserServiceImpl;

@SocketRequestHandler(desc = "获取用户列表", command = Command.USR_LIST)
public class UserListHandler extends SocketBaseHandler  {

	enum UserListType {
		LatestLogin,
		RecentContact
	}
	
	private UserService userService = new UserServiceImpl();
	private RoomService roomService = new RoomServiceImpl();
	private LiveService liveService = new LiveServiceImpl();
	
	@Override
	public CommandOut handle(String userSid, String reqData) throws Exception {
		appendLog("--UserListHandler(获取用户列表)--");
		UserListParams in = JSON.parseObject(reqData, UserListParams.class);
		
		int type = in.type;
		int index = in.index;
		int size = in.size; 
		appendLog("起始游标(index):" + index);
		appendLog("分页数量(size):" + size);
		appendLog("列表类型(type):" + type);
		List<RespUser> respUsers = new ArrayList<>();
		if (type == UserListType.LatestLogin.ordinal()) {
			List<User> users = new ArrayList<>();
			if (in.type == UserListType.LatestLogin.ordinal()) {
				users = userService.listByLatestLogin(index, size);
			}
			for (User u : users) {
				if (u.getSid().equals(userSid)) {
					// 过滤自己
					continue;
				}
				RespUser ru = new RespUser();
				ru.convert(u);
				Live live = liveService.findLiveByUser(u.getSid());
				if (live != null) {
					ru.liveSid = live.getSid();
				}
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
		} else if (type == UserListType.RecentContact.ordinal()) {
			List<User> users = userService.listContacts(userSid, index, size);
			for (User u : users) {
				if (u.getSid().equals(userSid)) {
					// 过滤自己
					continue;
				}
				RespUser ru = new RespUser();
				Live live = liveService.findLiveByUser(u.getSid());
				if (live != null) {
					ru.liveSid = live.getSid();
				}
			
				ru.convert(u);
				respUsers.add(ru);
			}
		}
		
		PageCommandOut<RespUser> out = new PageCommandOut<>(Command.USR_LIST, in);
		out.setData(respUsers);
		return out;
	}
	
	public static class UserListParams extends PageCommandIn {
		public int type;
	}
}
