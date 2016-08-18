package com.galive.logic.network.http.handler;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.network.http.HttpRequestHandler;
import com.galive.logic.network.platform.wx.WXUserInfoResp;
import com.galive.logic.service.AccountService;
import com.galive.logic.service.AccountServiceImpl;

@HttpRequestHandler(desc = "微信获取用户信息", command = Command.USR_LOGIN)
public class LoginHandler extends HttpBaseHandler {
	
	private AccountService accountService = new AccountServiceImpl();
	
	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--WechatUserInfoHandler(微信获取用户信息)--");
		LoginIn in = JSON.parseObject(reqData, LoginIn.class);
		
		WXUserInfoResp wx = accountService.reqWechatUserInfo(in.code);
		LoginOut out = new LoginOut();
		out.userInfo = wx;
		return out;
	}
	
	public static class LoginIn {
		public String code;
	}

	public static class LoginOut extends CommandOut {

		public LoginOut() {
			super(Command.USR_LOGIN);
		}

		public WXUserInfoResp userInfo;
	}
	
}
