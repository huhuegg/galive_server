package com.galive.logic.network.socket.handler;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Live;
import com.galive.logic.model.User;
import com.galive.logic.network.model.RespUser;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.push.LiveMessagePush;
import com.galive.logic.service.LiveService;
import com.galive.logic.service.LiveServiceImpl;
import com.galive.logic.service.UserService;
import com.galive.logic.service.UserServiceImpl;

@SocketRequestHandler(desc = "发送直播消息", command = Command.LIVE_MESSAGE_SEND)
public class LiveMessageSendHandler extends SocketBaseHandler {

	private UserService userService = new UserServiceImpl();
	private LiveService liveService = new LiveServiceImpl();

	@Override
	public CommandOut handle(String userSid, String reqData) throws Exception {
		appendLog("--LiveMessageSendHandler(发送直播消息)--");
		
		LiveMessageSendIn in = JSON.parseObject(reqData, LiveMessageSendIn.class);
		
		String content = in.content;
		appendLog("文本内容(content):" + content);
		
		if (content.length() > 500) {
			throw new LogicException("文本内容过长。");
		}
		
		Live live = liveService.findLiveByAudience(userSid);
		if (live != null) {
			User sender = userService.findUserBySid(userSid);
			String senderDesc = sender.desc();
			appendLog(senderDesc + live.desc());
			appendLog(senderDesc + "发送直播消息:" + content);

			// 推送
			List<String> audienceSids = liveService.listAllAudiences(live.getSid());
			LiveMessagePush push = new LiveMessagePush();
			push.content = in.content;
			RespUser respSender = new RespUser();
			respSender.convert(sender);
			push.sender = respSender;
			String pushMessage = push.socketResp();
			appendLog("LIVE_MESSAGE_PUSH:" + pushMessage);
			appendLog("推送用户数量:" + audienceSids.size());
			for (String sid : audienceSids) {
				if (!sid.equals(userSid)) {
					if (userService.isOnline(sid)) {
						pushMessage(sid, pushMessage);
					}
				}
			}
		}
		
		CommandOut out = new CommandOut(Command.LIVE_MESSAGE_SEND);
		return out;

	}

	public static class LiveMessageSendIn {
		public String content;
	}
}
