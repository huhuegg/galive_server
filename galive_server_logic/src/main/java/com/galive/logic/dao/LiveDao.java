package com.galive.logic.dao;

import java.util.List;

public interface LiveDao {
	
	
	public void saveLiveCreator(String liveSid, String account);
	
	public void saveLiveForCreator(String liveSid, String account);
	
	public String findLiveCreator(String liveSid);

	public String findLiveByCreator(String account);
	
	public String removeLiveCreator(String creator);
	
	public String removeLiveForCreator(String liveSid);
	
	
	public void saveLiveMember(String liveSid, String account);
	
	public void saveLiveForMember(String liveSid, String account);
	
	public String findLiveByMember(String account);
	
	public List<String> findLiveMembers(String liveSid);
	
	public List<String> removeLiveMember(String liveSid, String account);
	
	public void removeLiveForMember(String liveSid, String account);
	
	public List<String> removeLiveMembers(String liveSid);
	
	public boolean liveExsit(String liveSid);
	
	
//	
//	public Live findLive(String liveSid);
//
//	public Live findLiveByAccount(String account);
//	
//	public Live findLiveByAudienceSid(String audienceSid);
//	
//	public void insertToLiveListByLatestLiveAt(String liveSid);
//	
//	public List<String> listByLatestLiveTime(int start, int end);
//	
//	public void saveAudience(String liveSid, String userSid, boolean isPresenter);
//	
//	public void removeAudience(String userSid);
//	
//	public void removeAllAudiences(String liveSid);
//	
//	public List<String> listAudiences(String liveSid, int start, int end);
//	
//	public long countAudiences(String liveSid);
//	
//	public long[] incrLike(String liveSid, String userSid);
//	
//	public void clearLikeNum(String liveSid);
//	
//	public long[] likeNum(String liveSid);
//	
//	public long latestLikeTime(String liveSid, String userSid);
	
	
//	public Live findLive(String liveSid);
//	
//	public Live saveLive(Live live);
//	
//	public void saveLiveOwner(String account, String liveSid);
//	
//	public void saveLiveMember(String account, String liveSid);
//	
//	public Live findLiveByOwner(String account);
//	
//	public Live findLiveByMember(String account);
}
