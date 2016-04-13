package com.galive.logic.service;

import com.galive.logic.dao.LoggerCache;
import com.galive.logic.dao.LoggerCacheImpl;

public class LoggerServiceImpl implements LoggerService {

	private LoggerCache loggerCache = new LoggerCacheImpl();
	
	@Override
	public void saveLogicLog(String log) {
		loggerCache.saveLogicLog(log);
	}

}
