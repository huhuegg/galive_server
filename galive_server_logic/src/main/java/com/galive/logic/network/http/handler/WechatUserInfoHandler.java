package com.galive.logic.network.http.handler;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.network.http.HttpRequestHandler;
import com.galive.logic.network.platform.wx.WXUserInfoResp;
import com.galive.logic.service.AccountService;
import com.galive.logic.service.AccountServiceImpl;

@HttpRequestHandler(desc = "微信获取用户信息", command = Command.WECHAT_USR_INFO)
public class WechatUserInfoHandler extends HttpBaseHandler {
	
	private AccountService accountService = new AccountServiceImpl();
	
	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--WechatUserInfoHandler(微信获取用户信息)--");
		WechatUserInfoIn in = JSON.parseObject(reqData, WechatUserInfoIn.class);
		
		//WXUserInfoResp wx = accountService.reqWechatUserInfo(in.code);
		WechatUserInfoOut out = new WechatUserInfoOut();
		//out.userInfo = wx;
		return out;
	}
	
	public static class WechatUserInfoIn {
		public String code;
	}

	public static class WechatUserInfoOut extends CommandOut {

		public WechatUserInfoOut() {
			super(Command.WECHAT_USR_INFO);
		}

		public WXUserInfoResp userInfo;
	}
	
}
