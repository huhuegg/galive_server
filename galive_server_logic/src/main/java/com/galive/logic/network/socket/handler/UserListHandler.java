package com.galive.logic.network.socket.handler;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.common.protocol.PageParams;
import com.galive.common.protocol.PageCommandOut;
import com.galive.logic.model.Room;
import com.galive.logic.model.User;
import com.galive.logic.network.model.RespUser;
import com.galive.logic.network.socket.SocketRequestHandler;

@SocketRequestHandler(desc = "获取用户列表", command = Command.USR_LIST)
public class UserListHandler extends SocketBaseHandler  {

	enum UserListType {
		LatestLogin
	}
	
	private static Logger logger = LoggerFactory.getLogger(UserListHandler.class);
	
	@Override
	public String handle(String userSid, String reqData) {
		try {
			logger.debug("获取用户列表|" + userSid + "|" + reqData);
			UserListParams in = JSON.parseObject(reqData, UserListParams.class);
			
			List<User> users = new ArrayList<>();
			if (in.type == UserListType.LatestLogin.ordinal()) {
				users = userService.listByLatestLogin(in.index, in.size);
				logger.debug("获取用户列表|查询用户数:" + users.size());
			}

			List<RespUser> respUsers = new ArrayList<>();
			for (User u : users) {
				if (u.getSid().equals(userSid)) {
					// 过滤自己
					continue;
				}
				RespUser ru = RespUser.convert(u);
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
			logger.debug("获取用户列表|" + resp);
			return resp;
		} catch (Exception e) {
			logger.error(e.getMessage());
			String resp = respFail(null);
			return resp;
		}
	}
	
	private String respFail(String message) {
		String resp = CommandOut.failureOut(Command.USR_LIST, message).httpResp();
		logger.error("获取用户列表失败|" + resp);
		return resp;
	}
	
	public static class UserListParams extends PageParams {
		public int type;
	}
}
