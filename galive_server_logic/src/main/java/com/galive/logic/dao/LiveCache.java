package com.galive.logic.dao;

import java.util.List;

import com.galive.logic.model.Live;

public interface LiveCache {

	public Live saveLive(Live live);
	
	public Live findLive(String liveSid);

	public Live findLiveByOwnerSid(String ownerSid);
	
	public Live findLiveByAudienceSid(String audienceSid);
	
	public void insertToLiveListByLatestLiveAt(String liveSid);
	
	public List<String> listByLatestLiveTime(int start, int end);
	
	public void saveAudience(String liveSid, String userSid);
	
	public void removeAudience(String userSid);
	
	public List<String> listAudience(String liveSid, int start, int end);
}
