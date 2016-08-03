package com.galive.logic.network.socket.handler;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.common.protocol.PageCommandIn;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.LiveService;
import com.galive.logic.service.LiveServiceImpl;

@SocketRequestHandler(desc = "直播观众列表", command = Command.LIVE_AUDIENCE_LIST)
public class LiveAudienceListHandler extends SocketBaseHandler  {
	
	private LiveService liveService = new LiveServiceImpl();
	
	@Override
	public CommandOut handle(String userSid, String reqData) throws Exception {
		appendLog("--LiveAudienceListHandler(直播观众列表)--");
		LiveAudienceListIn in = JSON.parseObject(reqData, LiveAudienceListIn.class);
		
		String liveSid = in.liveSid;
		int index = in.index;
		int size = in.size;
		appendLog("直播id(liveSid):" + liveSid);
		appendLog("起始游标(index):" + index);
		appendLog("分页数量(size):" + size);
		
//		List<User> users = liveService.listAudiences(liveSid, index, size);
//		List<RespUser> respUsers = new ArrayList<>();
//		for (User u : users) {
//			RespUser respUser = new RespUser();
//			respUser.convert(u);
//			respUsers.add(respUser);
//		}
		//PageCommandOut<RespUser> out = new PageCommandOut<>(Command.LIVE_AUDIENCE_LIST, in);
		//out.setData(respUsers);
		return null;
	}
	
	public static class LiveAudienceListIn extends PageCommandIn {
		public String liveSid = "";
	}
}
