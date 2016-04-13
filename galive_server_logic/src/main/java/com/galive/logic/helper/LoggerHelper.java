package com.galive.logic.helper;

public class LoggerHelper {

	public static void appendLog(String log, StringBuffer logBuffer) {
		if (logBuffer != null) {
			logBuffer.append("|" + log);
			logBuffer.append("\n");
		}
	}
	
	public static void appendSplit(StringBuffer logBuffer) {
		if (logBuffer != null) {
			logBuffer.append("\n************************************************************************************************\n");
		}
	}
	
	public static String loggerString(StringBuffer logBuffer) {
		return logBuffer == null ? "" : logBuffer.toString();
	}
	
}
