package com.galive.logic.network.socket.handler;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.common.protocol.PageCommandIn;
import com.galive.common.protocol.PageCommandOut;
import com.galive.logic.model.User;
import com.galive.logic.network.model.RespUser;
import com.galive.logic.network.socket.SocketRequestHandler;

@SocketRequestHandler(desc = "获取用户列表", command = Command.USR_LIST)
public class UserListHandler extends SocketBaseHandler  {

	private static Logger logger = LoggerFactory.getLogger(UserListHandler.class);
	
	@Override
	public String handle(String userSid, String reqData) {
		try {
			logger.debug("获取用户列表|" + userSid + "|" + reqData);
			PageCommandIn in = JSON.parseObject(reqData, PageCommandIn.class);
			
			List<User> users = userService.listByLatestLogin(in.index, in.index + in.size - 1);
			List<RespUser> respUsers = new ArrayList<>();
			for (User u : users) {
				RespUser ru = RespUser.convertFromUser(u);
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
}
