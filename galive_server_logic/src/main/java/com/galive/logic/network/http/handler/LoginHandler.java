package com.galive.logic.network.http.handler;

import java.util.HashMap;
import java.util.Map;

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
import com.galive.logic.model.account.PlatformAccountWeChat;
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
		Map<String, Object> params = new HashMap<>();
		String nickname = "";
		String avatar = "";
		String platformSid = in.platformSid;
		params.put("platformSid", platformSid);
		appendLog("platformSid(platformSid):" + platformSid);
		switch (platform) {
		case Guest:
			String guestName = in.guestName;
			appendLog("guestName(游客名):" + guestName);
			params.put("name", guestName);
			platformAccount = accountService.login(accountSid, platform, params);
			nickname = guestName;
			break;
		case WeChat:
			String code = in.wechatCode;
			appendLog("微信code(wechatCode):" + code);
			params.put("wechatCode", code);
			platformAccount = accountService.login(accountSid, platform, params);
			nickname = ((PlatformAccountWeChat) platformAccount).getNickname();
			avatar = ((PlatformAccountWeChat) platformAccount).getHeadimgurl();
			break;
		}
		
		String token = accountService.generateToken(platformAccount.getAccountSid());
		
		Account act = accountService.findAndCheckAccount(platformAccount.getAccountSid());
		act.setAvatar(avatar);
		act.setNickname(nickname);
		act.setPlatformSid(platformAccount.getSid());
		
		act.setPlatform(platform);
	
		out.token =  token;
		out.account = act;
		out.socketConfig = ApplicationConfig.getInstance().getSocketConfig();
		out.logicConfig = ApplicationConfig.getInstance().getLogicConfig();
		return out;
	}
	
	public static class LoginIn {

		// 账号id 可选 非空且platform不为Guest时做绑定
		public String accountSid;
		
		// 账号平台[String] Guest WeChat 
		public Platform platform;
		
		// 微信code platform为WeChat时 与platformSid二选一 获取用户信息用
		public String wechatCode;
		
		// 自动登录 直接获取用户信息用 免去授权过程
		public String platformSid;
		
		// 游客名 platform为Guest时 必填
		public String guestName;
	}

	public static class LoginOut extends CommandOut {

		public LoginOut() {
			super(Command.USR_LOGIN);
		}

		public SocketConfig socketConfig;
		public LogicConfig logicConfig;
		public Account account;
		public Meeting meeting;
		public String token;
	}
	
}
