package com.galive.logic.network.socket.handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.network.socket.ChannelManager;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.LiveService;
import com.galive.logic.service.LiveServiceImpl;

@SocketRequestHandler(desc = "用户下线", command = Command.OFFLINE)
public class OfflineHandler extends SocketBaseHandler {

	private final static ExecutorService service = Executors.newCachedThreadPool();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--OfflineHandler(用户下线)--");

		// 清除直播信息
//		liveService.clearLiveForAccount(account);
//		CommandOut out = new CommandOut(Command.OFFLINE);
//		return out;
		service.execute(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(20000);
					// 用户不在线则清除相关房间信息 防止房间被占用
					LiveService liveService = new LiveServiceImpl();
					liveService.clearLiveForAccount(account);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		return null;
	}

}
