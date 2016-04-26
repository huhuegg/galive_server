package com.galive.logic.network.http.handler;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandIn;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.network.http.HttpRequestHandler;
import com.galive.logic.service.UserServiceImpl;

@HttpRequestHandler(desc = "修改用户信息", command = Command.USR_INFO_MODIFY)
public class UserInfoModifyHandler extends HttpBaseHandler {

	public enum UserInfoModifyType {
		DeviceToken;
	}
	
	private UserServiceImpl userService = new UserServiceImpl();
	
	@Override
	public String handle(String userSid, String reqData) throws Exception {
		appendLog("--UserInfoModifyHandler(修改用户信息)--");
		
		UserInfoModifyIn req = JSON.parseObject(reqData, UserInfoModifyIn.class);
		
		UserInfoModifyType type = req.type;
		appendLog("type:" + type);
		
		if (type == UserInfoModifyType.DeviceToken) {
			// 更新deviceToken
			userService.updateDeviceToken(userSid, req.deviceToken);
		}
		CommandOut out = new CommandOut(Command.USR_INFO_MODIFY);
		String resp = out.httpResp();
		return resp;
		
	}

	public static class UserInfoModifyIn extends CommandIn {
		public UserInfoModifyType type;
		public String deviceToken;
	}

}
