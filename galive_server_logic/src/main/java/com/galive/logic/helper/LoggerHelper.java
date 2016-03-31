package com.galive.logic.helper;

import java.io.File;
import org.slf4j.LoggerFactory;

import com.galive.logic.ApplicationMain;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

public class LoggerHelper {

	
	public static void resetLogConfigPath() {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		JoranConfigurator configurator = new JoranConfigurator();
		configurator.setContext(lc);
		lc.reset();
		String name = File.separator + ApplicationMain.getInstance().getMode().name + File.separator + "logback.xml";
		String path = LoggerHelper.class.getResource(name).getPath();
		try {
			configurator.doConfigure(path);
		} catch (JoranException e) {
			e.printStackTrace();
		}
		StatusPrinter.printInCaseOfErrorsOrWarnings(lc);
	}
}
