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
	
	public void saveAudience(String liveSid, String userSid, boolean isPresenter);
	
	public void removeAudience(String userSid);
	
	public void removeAllAudiences(String liveSid);
	
	public List<String> listAudiences(String liveSid, int start, int end);
	
	public long countAudiences(String liveSid);
	
	public long[] incrLike(String liveSid, String userSid);
	
	public void clearLikeNum(String liveSid);
	
	public long[] likeNum(String liveSid);
	
	public long latestLikeTime(String liveSid, String userSid);
}
