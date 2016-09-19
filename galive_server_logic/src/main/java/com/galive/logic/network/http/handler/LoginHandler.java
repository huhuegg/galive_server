package com.galive.logic.network.http.handler;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.config.LogicConfig;
import com.galive.logic.config.SocketConfig;
import com.galive.logic.model.Meeting;
import com.galive.logic.model.account.Account;
import com.galive.logic.model.account.Platform;
import com.galive.logic.model.account.PlatformAccount;
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
		String accountSid = in.accountSid;
		appendLog("绑定用户id(accountSid):" + platform);
		
		if (platform == null) {
			CommandOut out = CommandOut.failureOut(Command.USR_LOGIN, "平台不存在。");
			return out;
		}
		LoginOut out = new LoginOut();
		PlatformAccount platformAccount = null;
		switch (platform) {
		case Guest:
			String guestName = in.guestName;
			appendLog("guestName(游客名):" + guestName);
			platformAccount = accountService.login(accountSid, platform, guestName);
		case WeChat:
			String code = in.wechatCode;
			appendLog("微信code(wechatCode):" + code);
			platformAccount = accountService.login(accountSid, platform, code);
			break;
		}
		
		String token = accountService.generateToken(platformAccount.getAccountSid());
		
		Account act = accountService.findAccount(platformAccount.getAccountSid());
	
		out.platformInfo = platformAccount;
		out.token =  token;
		out.account = act;
		out.socketConfig = ApplicationConfig.getInstance().getSocketConfig();
		out.logicConfig = ApplicationConfig.getInstance().getLogicConfig();
		return out;
	}
	
	public static class LoginIn {

		public String accountSid;
		public Platform platform;
		public String wechatCode;
		public String guestName;
	}

	public static class LoginOut extends CommandOut {

		public LoginOut() {
			super(Command.USR_LOGIN);
		}

		public SocketConfig socketConfig;
		public LogicConfig logicConfig;
		public Account account;
		public PlatformAccount platformInfo;
		public Meeting meeting;
		public String token;
	}
	
}
