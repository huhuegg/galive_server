package com.galive.logic.service;

import java.util.List;

import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Live;

public interface LiveService {

	public Live startLive(String userSid) throws LogicException;
	
	public void stopLive();
	
	public List<Live> listByLatestLiveTime(int index, int size);
}
