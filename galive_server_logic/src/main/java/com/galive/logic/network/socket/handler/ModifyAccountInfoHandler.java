package com.galive.logic.network.socket.handler;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandIn;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Account;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.AccountService;
import com.galive.logic.service.AccountServiceImpl;

@SocketRequestHandler(desc = "更改用户信息", command = Command.MODIFY_ACCOUNT_INFO)
public class ModifyAccountInfoHandler extends SocketBaseHandler {

	private AccountService accountService = new AccountServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--ModifyAccountInfoHandler(更改用户信息)--");
		ModifyAccountInfoIn in = JSON.parseObject(reqData, ModifyAccountInfoIn.class);
		
//		Account act = accountService.findAccount(account);
//		
//		if (in.state != null) {
//			act.setState(in.state);
//			appendLog("state:" + in.state);
//		}
//		
//		accountService.saveAccount(act);
//
		CommandOut out = new CommandOut(Command.MODIFY_ACCOUNT_INFO);
		return out;
	}

	public static class ModifyAccountInfoIn extends CommandIn {
		public Integer state;
	}
}
