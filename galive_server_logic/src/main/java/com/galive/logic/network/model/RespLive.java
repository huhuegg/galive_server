package com.galive.logic.network.model;

import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.model.Live;

public class RespLive {

	public String sid;
	
	public String name;
	
	public long createAt;
	
	public long latestLiveAt;
	
	public String thumbnail;
	
	public String rtmpUrl;
	
	public String hlsUrl;
	
	public void convert(Live live) {
		this.name = live.getName();
		this.sid = live.getSid();
		this.createAt = live.getCreateAt();
		this.latestLiveAt = live.getLatestLiveAt();
		this.thumbnail = "http://img1.imgtn.bdimg.com/it/u=1990110392,1863050465&fm=21&gp=0.jpg";
		this.rtmpUrl = ApplicationConfig.getInstance().getLiveConfig().getRtmpUrl() + "/" + sid;
		this.hlsUrl = ApplicationConfig.getInstance().getLiveConfig().getHlsUrl() + "/" + sid;
	}
}
