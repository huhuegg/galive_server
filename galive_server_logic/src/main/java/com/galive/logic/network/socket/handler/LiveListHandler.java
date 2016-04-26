package com.galive.logic.network.socket.handler;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.PageParams;
import com.galive.common.protocol.PageCommandOut;
import com.galive.logic.model.Live;
import com.galive.logic.network.model.RespLive;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.LiveService;
import com.galive.logic.service.LiveServiceImpl;

@SocketRequestHandler(desc = "直播间列表", command = Command.LIVE_LIST)
public class LiveListHandler extends SocketBaseHandler  {
	
	private LiveService liveService = new LiveServiceImpl();
	
	@Override
	public String handle(String userSid, String reqData) {
		appendLog("--LiveListHandler(获取直播列表)--");
		PageParams in = JSON.parseObject(reqData, PageParams.class);
		
		int index = in.index;
		int size = in.size; 
		appendLog("起始游标(index):" + index);
		appendLog("分页数量(size):" + size);
		
		List<Live> lives = liveService.listByLatestLiveTime(index, size);
		List<RespLive> respLives = new ArrayList<>();
		for (Live live : lives) {
			RespLive respLive = new RespLive();
			respLive.convert(live);
			respLives.add(respLive);
		}
		PageCommandOut<RespLive> out = new PageCommandOut<>(Command.LIVE_LIST, in);
		out.setData(respLives);
		String resp = out.socketResp();
		return resp;
	}
	
}
