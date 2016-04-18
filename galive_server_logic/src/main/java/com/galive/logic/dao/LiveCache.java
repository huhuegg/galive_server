package com.galive.logic.dao;

import java.util.List;

import com.galive.logic.model.Live;

public interface LiveCache {

	public Live saveLive(Live live);
	
	public Live findLive(String liveSid);

	public Live findLiveByOwnerSid(String ownerSid);
	
	public void insertToLiveListByLatestLiveAt(String liveSid);
	
	public List<Live> listByLatestLiveTime(int start, int end);
}
