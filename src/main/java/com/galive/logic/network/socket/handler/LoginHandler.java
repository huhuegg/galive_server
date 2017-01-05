package com.galive.logic.network.socket.handler;

import com.alibaba.fastjson.JSON;
import com.galive.logic.network.protocol.Command;
import com.galive.logic.network.protocol.CommandOut;
import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.config.SocketConfig;
import com.galive.logic.model.Room;
import com.galive.logic.model.account.Account;
import com.galive.logic.model.account.Platform;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.AccountService;
import com.galive.logic.service.AccountServiceImpl;
import com.galive.logic.service.RoomService;
import com.galive.logic.service.RoomService.FindRoomBy;
import com.galive.logic.service.RoomServiceImpl;

@SocketRequestHandler(desc = "用户登录", command = Command.USR_LOGIN)
public class LoginHandler extends WebSocketBaseHandler {
	
	private AccountService accountService = new AccountServiceImpl();
	private RoomService roomService = new RoomServiceImpl();
	
	
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
		Room room = roomService.findRoom(FindRoomBy.Owner, act.getSid());
		if (room == null) {
			room = roomService.findRoom(FindRoomBy.Member, act.getSid());
		}
		
		String token = accountService.generateToken(act.getSid());
		
		LoginOut out = new LoginOut();
	
		out.token =  token;
		out.account = act;
		if (room != null) {
			out.room = room;
		}

		out.socketConfig = ApplicationConfig.getInstance().getSocketConfig();
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
		String platformParams;
	}

	public static class LoginOut extends CommandOut {

		LoginOut() {
			super(Command.USR_LOGIN);
		}

		public SocketConfig socketConfig;
		public Account account;
		public Room room;
		public String token;
	}
	
}
