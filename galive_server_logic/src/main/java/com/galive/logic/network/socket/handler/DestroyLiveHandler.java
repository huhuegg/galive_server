package com.galive.logic.network.socket.handler;

import java.util.List;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Live;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.DestroyLivePush;
import com.galive.logic.service.LiveService;
import com.galive.logic.service.LiveServiceImpl;

@SocketRequestHandler(desc = "创建直播", command = Command.DESTROY_LIVE)
public class DestroyLiveHandler extends SocketBaseHandler {

	private LiveService liveService = new LiveServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--DestroyLiveHandler(创建直播)--");

		Live live = liveService.destroyLive(account);

		DestroyLivePush push = new DestroyLivePush();
		push.account = account;
		String pushContent = push.socketResp();

		List<String> accounts = live.getMemberAccounts();
		for (String act : accounts) {
			pushMessage(act, pushContent);
		}

		CommandOut out = new CommandOut(Command.DESTROY_LIVE);
		return out;
	}

}
