package com.galive.logic.network.http.handler;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandIn;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.config.SocketConfig;
import com.galive.logic.network.http.HttpRequestHandler;
import com.galive.logic.service.AccountService;
import com.galive.logic.service.AccountServiceImpl;
import com.galive.logic.service.LiveService;
import com.galive.logic.service.LiveServiceImpl;

@HttpRequestHandler(desc = "获取token", command = Command.REQ_TOKEN)
public class ReqTokenHandler extends HttpBaseHandler {
	
	private LiveService liveService = new LiveServiceImpl();
	private AccountService accountService = new AccountServiceImpl();
	
	@Override
	public CommandOut handle(String account, String channel, String reqData) throws Exception {
		appendLog("--ReqTokenHandler(用户获取token)--");
		
		String token = accountService.generateToken(account, channel);
		
		// TODO
		// 清除直播状态，处理直播房间剩余人数
		
		
	
		ReqTokenOut out = new ReqTokenOut();
		
		out.token =  token;
		out.socket = ApplicationConfig.getInstance().getSocketConfig();
		return out;
	}

	public static class ReqTokenIn extends CommandIn {
		
	}
	
	public static class ReqTokenOut extends CommandOut {

		public ReqTokenOut() {
			super(Command.REQ_TOKEN);
		}

		public SocketConfig socket;
		public String token;
	}
	
}
