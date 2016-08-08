package com.galive.logic.service;

import java.util.List;

import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Live;

public interface LiveService {
	
	public Live createLive(String account) throws LogicException;
	
	public Live joinLive(String account, String liveSid) throws LogicException;
	
	public Live leaveLive(String account) throws LogicException;
	
	public Live destroyLive(String account) throws LogicException;
	
	public List<String> listLiveMembersByAccount(String account) throws LogicException;
	
}
