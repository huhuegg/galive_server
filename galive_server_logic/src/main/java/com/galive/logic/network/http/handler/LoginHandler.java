package com.galive.logic.network.http.handler;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.config.LogicConfig;
import com.galive.logic.config.SocketConfig;
import com.galive.logic.model.Platform;
import com.galive.logic.model.PlatformAccount;
import com.galive.logic.model.PlatformAccountWeChat;
import com.galive.logic.network.http.HttpRequestHandler;
import com.galive.logic.service.AccountService;
import com.galive.logic.service.AccountServiceImpl;

@HttpRequestHandler(desc = "用户登录", command = Command.USR_LOGIN)
public class LoginHandler extends HttpBaseHandler {
	
	private AccountService accountService = new AccountServiceImpl();
	
	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--LoginHandler(用户登录)--");
		LoginIn in = JSON.parseObject(reqData, LoginIn.class);
		
		Platform platform = in.platform;
		appendLog("登录平台(platform):" + platform);
		
		if (platform == null) {
			CommandOut out = CommandOut.failureOut(Command.USR_LOGIN, "平台不存在。");
			return out;
		}
		LoginOut out = new LoginOut();
		String tokenSid = "";
		switch (platform) {
		case WeChat:
			String code = in.wechatCode;
			appendLog("code(wechatCode):" + code);
			PlatformAccountWeChat act = accountService.loginWechat(code);
			out.account = act;
			tokenSid = act.getSid();
			break;
		}
		
		String token = accountService.generateToken(tokenSid);
		out.token =  token;
		out.socketConfig = ApplicationConfig.getInstance().getSocketConfig();
		out.logicConfig = ApplicationConfig.getInstance().getLogicConfig();
		return out;
	}
	
	public static class LoginIn {

		public Platform platform;
		public String wechatCode;
	}

	public static class LoginOut extends CommandOut {

		public LoginOut() {
			super(Command.USR_LOGIN);
		}

		public SocketConfig socketConfig;
		public LogicConfig logicConfig;
		public PlatformAccount account;
		public String token;
	}
	
}
