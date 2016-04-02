package com.galive.logic.network.socket.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandIn;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.User;
import com.galive.logic.network.socket.SocketRequestHandler;

@SocketRequestHandler(desc = "修改用户信息", command = Command.USR_INFO_MODIFY)
public class UserInfoModifyHandler extends SocketBaseHandler {

	private static Logger logger = LoggerFactory.getLogger(UserInfoModifyHandler.class);

	@Override
	public CommandOut commandProcess(String userSid, String reqData) {
		logger.debug("修改用户信息|" + reqData);
		UserInfoModifyIn req = JSON.parseObject(reqData, UserInfoModifyIn.class);
		User u = User.findByUsername(userSid);
		if (u == null) {
			return CommandOut.failureOut(Command.USR_INFO_MODIFY, "用户不存在");
		}
		int type = req.type;
		if (type == UserInfoModifyType.DeviceToken.ordinal()) {
			// 更新deviceToken
			String token = req.deviceToken;
			User.updateDeviceToken(userSid, token);
		}

		CommandOut out = new CommandOut(Command.USR_INFO_MODIFY);
		return out;
	}

	public static class UserInfoModifyIn extends CommandIn {
		public int type;
		public String deviceToken;
	}

	public enum UserInfoModifyType {
		DeviceToken;
	}
}
