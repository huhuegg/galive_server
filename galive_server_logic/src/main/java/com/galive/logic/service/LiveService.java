package com.galive.logic.service;

import java.util.List;
import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Live;
import com.galive.logic.model.User;

public interface LiveService {
	
	public Live findLive(String liveSid) throws LogicException;

	public Live findLiveByUser(String userSid);
	
	public Live findLiveByAudience(String userSid);
	
	public Live startLive(String userSid) throws LogicException;
	
	public Live stopLive(String userSid, String actionRecordUrl) throws LogicException;
	
	public List<Live> listByLatestLiveTime(int index, int size);
	
	public Live joinLive(String liveSid, String userSid) throws LogicException;
	
	public Live leaveLive(String userSid) throws LogicException;
	
	public List<User> listAudiences(String liveSid, int index, int size) throws LogicException;
	
	public List<String> listAllAudiences(String liveSid) throws LogicException;
	
	public long[] doLike(String liveSid, String userSid) throws LogicException;
	
	public long[] likeNums(String liveSid) throws LogicException;
}
