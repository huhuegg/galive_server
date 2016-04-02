package com.galive.logic.network.http.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandIn;
import com.galive.common.protocol.CommandOut;
import com.galive.common.protocol.RetCode;
import com.galive.logic.model.User;

public abstract class HttpBaseHandler {

	private static Logger logger = LoggerFactory.getLogger(HttpBaseHandler.class);
	
	public abstract CommandOut handle(String userSid, String params);
	
	public String handle(CommandIn in) {
		CommandOut out = null;
		String command = in.getCommand();
		String userSid = in.getUserSid();
		String token = in.getToken();
		String params = in.getParams();
		try {
			if ((!command.equals(Command.USR_LOGIN) && !command.equals(Command.USR_REGISTER))  && !User.verifyToken(userSid, token)) {
				// 验证token
				out = CommandOut.failureOut(command, "登录已过期");
				out.setRet_code(RetCode.TOKEN_EXPIRE);
			} else {
				out = handle(userSid, params);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户" + userSid + " 请求失败:" + e.getMessage());
			out = CommandOut.failureOut(command, e.getMessage());
		} 
		String respBody = out.toJson();
		logger.debug("响应数据|" + respBody);
		return respBody;
	}
}
