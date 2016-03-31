package com.galive.logic.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.common.protocol.RetCode;
import com.galive.logic.model.User;

public abstract class BaseHandler {

	private static Logger logger = LoggerFactory.getLogger(BaseHandler.class);
	
	public abstract CommandOut commandProcess(String userSid, String reqData);
	
	public String process(Command command, String userSid, String token, String reqData) {
		CommandOut out = null;
		try {
			if ((command != Command.USR_LOGIN && command != Command.USR_REGISTER)  && !User.verifyToken(userSid, token)) {
				// 验证token
				out = CommandOut.failureOut(command, "登录已过期");
				out.setRet_code(RetCode.TOKEN_EXPIRE);
			} else {
				out = commandProcess(userSid, reqData);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户" + userSid + " 请求失败:" + e.getMessage());
			out = CommandOut.failureOut(command, e.getMessage());
		} 
		String json = out.toJson();
		logger.debug("响应数据|" + json);
		return json;
	}
}
