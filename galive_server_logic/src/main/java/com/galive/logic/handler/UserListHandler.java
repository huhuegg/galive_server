package com.galive.logic.handler;

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

@LogicHandler(desc = "用户列表", command = Command.USR_LIST)
public class UserListHandler extends BaseHandler  {

	private static Logger logger = LoggerFactory.getLogger(UserListHandler.class);

	@Override
	public CommandOut commandProcess(String userSid, String reqData) {
		logger.debug("用户列表|" + userSid + "|" + reqData);
		PageCommandIn in = JSON.parseObject(reqData, PageCommandIn.class);
		
		List<User> users = User.listByLatestLogin(in.index, in.index + in.size - 1);
		List<RespUser> respUsers = new ArrayList<>();
		for (User u : users) {
			RespUser ru = RespUser.convertFromUser(u);
			respUsers.add(ru);
		}
		PageCommandOut<RespUser> out = new PageCommandOut<>(Command.USR_LIST, in);
		out.setData(respUsers);
		return out;
	}
}
