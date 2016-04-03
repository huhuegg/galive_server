package com.galive.logic.network.http.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandIn;
import com.galive.common.protocol.CommandOut;
import com.galive.common.protocol.RetCode;
import com.galive.logic.service.RoomServiceImpl;
import com.galive.logic.service.UserServiceImpl;

public abstract class HttpBaseHandler {

	private static Logger logger = LoggerFactory.getLogger(HttpBaseHandler.class);
	
	protected UserServiceImpl userService = new UserServiceImpl();
	protected RoomServiceImpl roomService = new RoomServiceImpl();
	
	public abstract String handle(String userSid, String params);
	
	public String handle(CommandIn in) {
		String resp = null;
		String command = in.getCommand();
		String userSid = in.getUserSid();
		String token = in.getToken();
		String params = in.getParams();
		try {
			if ((!command.equals(Command.USR_LOGIN) && !command.equals(Command.USR_REGISTER))  && !userService.verifyToken(userSid, token)) {
				// 验证token
				CommandOut out = CommandOut.failureOut(command, "登录已过期");
				out.setRet_code(RetCode.TOKEN_EXPIRE);
				resp = out.httpResp();
			} else {
				resp = handle(userSid, params);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户" + userSid + " 请求失败:" + e.getMessage());
			CommandOut out = CommandOut.failureOut(command, e.getMessage());
			resp = out.httpResp();
		} 
		return resp;
	}

}
