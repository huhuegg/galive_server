package com.galive.logic.network.socket.handler;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandIn;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Live;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.JoinLivePush;
import com.galive.logic.service.LiveService;
import com.galive.logic.service.LiveServiceImpl;

@SocketRequestHandler(desc = "加入直播", command = Command.JOIN_LIVE)
public class JoinLiveHandler extends SocketBaseHandler {

	private LiveService liveService = new LiveServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--JoinLiveHandler(加入直播)--");

		JoinLiveIn in = JSON.parseObject(reqData, JoinLiveIn.class);
		String liveSid = in.liveSid;
		appendLog("直播id(liveSid):" + liveSid);

		Live live = liveService.joinLive(account, liveSid);

		JoinLivePush push = new JoinLivePush();
		push.account = account;
		String pushContent = push.socketResp();
		
		
		List<String> accounts = live.getMemberAccounts();
		for (String act : accounts) {
			if (!act.equals(account)) {
				pushMessage(act, pushContent);
				appendLog("推送房间内成员:" + act + " " + pushContent);
			}
		}
		
		String owner = live.getOwnerAccount();
		if (!StringUtils.isEmpty(owner)) {
			pushMessage(owner, pushContent);
			appendLog("推送房主:" + owner + " " + pushContent);
		}
		CommandOut out = new CommandOut(Command.JOIN_LIVE);
		return out;
	}

	public static class JoinLiveIn extends CommandIn {
		public String liveSid = "";
	}
}
