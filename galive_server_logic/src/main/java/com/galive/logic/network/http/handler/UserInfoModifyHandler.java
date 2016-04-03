package com.galive.logic.network.http.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandIn;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.exception.LogicException;
import com.galive.logic.network.http.HttpRequestHandler;

@HttpRequestHandler(desc = "修改用户信息", command = Command.USR_INFO_MODIFY)
public class UserInfoModifyHandler extends HttpBaseHandler {

	public enum UserInfoModifyType {
		DeviceToken;
	}
	
	private static Logger logger = LoggerFactory.getLogger(UserInfoModifyHandler.class);

	@Override
	public String handle(String userSid, String reqData) {
		try {
			logger.debug("修改用户信息|" + reqData);
			UserInfoModifyIn req = JSON.parseObject(reqData, UserInfoModifyIn.class);
			
			int type = req.type;
			if (type == UserInfoModifyType.DeviceToken.ordinal()) {
				// 更新deviceToken
				userService.updateUserDeviceToken(userSid, req.deviceToken);
			}
			CommandOut out = new CommandOut(Command.USR_INFO_MODIFY);
			String resp = out.httpResp();
			logger.info("修改用户信息|" + resp);
			return resp;
		} catch (LogicException e) {
			logger.error(e.getMessage());
			String resp = respFail(e.getMessage());
			return resp;
		} catch (Exception e) {
			logger.error(e.getMessage());
			String resp = respFail(null);
			return resp;
		}
		
	}

	public static class UserInfoModifyIn extends CommandIn {
		public int type;
		public String deviceToken;
	}

	
	
	private String respFail(String message) {
		String resp = CommandOut.failureOut(Command.USR_LOGIN, message).httpResp();
		logger.error("修改用户信息失败|" + resp);
		return resp;
	}
}
