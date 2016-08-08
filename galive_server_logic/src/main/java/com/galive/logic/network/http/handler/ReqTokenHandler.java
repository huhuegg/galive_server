package com.galive.logic.network.http.handler;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.config.LogicConfig;
import com.galive.logic.config.SocketConfig;
import com.galive.logic.model.Account;
import com.galive.logic.network.http.HttpRequestHandler;
import com.galive.logic.service.AccountService;
import com.galive.logic.service.AccountServiceImpl;

@HttpRequestHandler(desc = "用户获取token", command = Command.REQ_TOKEN)
public class ReqTokenHandler extends HttpBaseHandler {
	
	private AccountService accountService = new AccountServiceImpl();
	
	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--ReqTokenHandler(用户获取token)--");
		
		Account act = new Account();
		act.setAccount(account);
		accountService.saveAccount(act);
		
		String token = accountService.generateToken(account);
		ReqTokenOut out = new ReqTokenOut();
		
		out.token =  token;
		out.socketConfig = ApplicationConfig.getInstance().getSocketConfig();
		out.logicConfig = ApplicationConfig.getInstance().getLogicConfig();
		return out;
	}

	public static class ReqTokenOut extends CommandOut {

		public ReqTokenOut() {
			super(Command.REQ_TOKEN);
		}

		public SocketConfig socketConfig;
		public LogicConfig logicConfig;
		public String token;
	}
	
}
