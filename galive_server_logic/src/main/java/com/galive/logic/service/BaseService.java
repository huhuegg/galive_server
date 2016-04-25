package com.galive.logic.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseService {

	private static Logger logger = LoggerFactory.getLogger(BaseService.class);
	
	protected StringBuffer logBuffer = new StringBuffer();
	
	public BaseService() {
		appendSplit();
	}
	
	@Override
	protected void finalize() throws Throwable {
		appendSplit();
		logger.info(loggerString());
		super.finalize();
	}
	
	protected void appendLog(String log) {
		if (logBuffer != null) {
			logBuffer.append("|" + log);
			logBuffer.append("\n");
		}
	}
	
	protected void appendSplit() {
		if (logBuffer != null) {
			logBuffer.append("\n************************************************************************************************\n");
		}
	}
	
	protected String loggerString() {
		return logBuffer == null ? "" : logBuffer.toString();
	}
}
