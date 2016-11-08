package com.galive.logic.network.http.handler;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.config.LogicConfig;
import com.galive.logic.config.SocketConfig;
import com.galive.logic.model.account.Account;
import com.galive.logic.model.account.Platform;
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
		
		
		String accountSid = in.accountSid;
		Platform platform = in.platform;
		String platformParams = in.platformParams;
		
		appendLog("用户id(accountSid):" + accountSid);
		appendLog("用户平台(platform):" + platform);
		appendLog("platformParams:" + platformParams);
		
		Account act = accountService.login(accountSid, platform, platformParams);
		
		String token = accountService.generateToken(act.getSid());
		
		LoginOut out = new LoginOut();
	
		out.token =  token;
		out.account = act;
		out.socketConfig = ApplicationConfig.getInstance().getSocketConfig();
		out.logicConfig = ApplicationConfig.getInstance().getLogicConfig();
		return out;
	}
	
	public static class LoginIn {

		// 账号id 可选 空值时自动注册
		public String accountSid;

		// *accountSid为空时必填* 
		// 账号平台[String] "Guest" - 游客自动注册; "WeChat" - 微信;
		public Platform platform;
		
		// *三方平台参数* 
		//	根据platform传不同的值
		// Guest 空字符""
		// WeChat 用户授权code
		public String platformParams;
	}

	public static class LoginOut extends CommandOut {

		public LoginOut() {
			super(Command.USR_LOGIN);
		}

		public SocketConfig socketConfig;
		public LogicConfig logicConfig;
		public Account account;
		public String token;
	}
	
}
