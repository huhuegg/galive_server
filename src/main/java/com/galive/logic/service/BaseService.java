package com.galive.logic.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galive.logic.exception.LogicException;

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
	
	protected LogicException makeLogicException(String error) {
		logBuffer.append(error);
		LogicException exception = new LogicException(error);
		return exception;
	}
}
