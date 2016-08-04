package com.galive.logic.service;

import com.galive.logic.model.Live;

public interface LiveService {
	
//	public Live findLive(String liveSid) throws LogicException;
//
//	public Live findLiveByUser(String userSid);
//	
//	public Live findLiveByAudience(String userSid);
//	
//	public Live startLive(String userSid) throws LogicException;
//	
//	public Live stopLive(String userSid, String actionRecordUrl) throws LogicException;
//	
//	public List<Live> listByLatestLiveTime(int index, int size);
//	
//	public Live joinLive(String liveSid, String userSid) throws LogicException;
//	
//	public Live leaveLive(String userSid) throws LogicException;
//	
//	public List<String> listAllAudiences(String liveSid) throws LogicException;
//	
//	public long[] doLike(String liveSid, String userSid) throws LogicException;
//	
//	public long[] likeNums(String liveSid) throws LogicException;
	
	public Live createLive(String account);
	
	public Live joinLive(String account, String liveSid);
	
	public void leaveLive(String account);
	
	public void destroyLive(String account);
}
