package com.galive.logic.log;

import org.slf4j.LoggerFactory;

import com.galive.logic.ApplicationMain;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

public class LogManager {

	
	public static void resetLogConfigPath() {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		JoranConfigurator configurator = new JoranConfigurator();
		configurator.setContext(lc);
		lc.reset();
		String name = "/" + ApplicationMain.getInstance().getMode().name + "/" + "logback.xml";
		String path = LogManager.class.getResource(name).getPath();
		try {
			configurator.doConfigure(path);
		} catch (JoranException e) {
			e.printStackTrace();
		}
		StatusPrinter.printInCaseOfErrorsOrWarnings(lc);
	}
}
