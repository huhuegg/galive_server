package com.galive.logic.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandIn;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.User;
import com.galive.logic.network.model.RespUser;

@LogicHandler(desc = "获取用户信息", command = Command.USR_INFO)
public class UserInfoHandler extends BaseHandler {

	private static Logger logger = LoggerFactory.getLogger(UserInfoHandler.class);

	@Override
	public CommandOut commandProcess(String userSid, String reqData) {
		logger.debug("获取用户信息|" + reqData);
		UserInfoRequest req = JSON.parseObject(reqData, UserInfoRequest.class);
		User u = User.findBySid(req.userSid);
		if (u == null) {
			return CommandOut.failureOut(Command.USR_INFO, "用户不存在");
		} 
		
		UserInfoOut out = new UserInfoOut();
		RespUser ru = RespUser.convertFromUser(u);
		out.user = ru;
		return out;
	}

	public static class UserInfoRequest extends CommandIn {
		public String userSid;
	}
	
	public static class UserInfoOut extends CommandOut {

		public RespUser user;
		
		public UserInfoOut() {
			super(Command.USR_INFO); 
		}
	}
}
