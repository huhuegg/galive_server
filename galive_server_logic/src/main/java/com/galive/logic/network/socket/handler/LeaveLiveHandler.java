package com.galive.logic.network.socket.handler;

import java.util.List;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Live;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.LeaveLivePush;
import com.galive.logic.service.LiveService;
import com.galive.logic.service.LiveServiceImpl;

@SocketRequestHandler(desc = "离开直播", command = Command.LEAVE_LIVE)
public class LeaveLiveHandler extends SocketBaseHandler {

	private LiveService liveService = new LiveServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--LeaveLiveHandler(直播观众列表)--");

		Live live = liveService.leaveLive(account);

		LeaveLivePush push = new LeaveLivePush();
		push.account = account;
		String pushContent = push.socketResp();

		List<String> accounts = live.getMemberAccounts();
		for (String act : accounts) {
			if (!act.equals(account))
				pushMessage(act, pushContent);
		}

		CommandOut out = new CommandOut(Command.DESTROY_LIVE);
		return out;
	}

}