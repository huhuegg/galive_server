package com.galive.logic.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.LoggerFactory;

import com.galive.logic.ApplicationMain;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

public class LogicHelper {

	private static final String SEPARATOR = "/";
	
	public static Properties loadProperties() throws IOException {
		Properties prop = new Properties();
		String name = SEPARATOR + ApplicationMain.get().getMode().name + SEPARATOR + "jdbc.properties";
		InputStream in = LogicHelper.class.getResourceAsStream(name);
		prop.load(in);
		return prop;
	}
	
	public static InputStream loadConfig() throws IOException {
		String name = SEPARATOR + ApplicationMain.get().getMode().name + SEPARATOR + "config.xml";
		InputStream in = LogicHelper.class.getResourceAsStream(name);
		return in;
	}
	
	public static void resetLogConfigPath() {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		JoranConfigurator configurator = new JoranConfigurator();
		configurator.setContext(lc);
		lc.reset();
		String name = SEPARATOR + ApplicationMain.get().getMode().name + SEPARATOR + "logback.xml";
		InputStream in = LogicHelper.class.getResourceAsStream(name);
		try {
			configurator.doConfigure(in);
		} catch (JoranException e) {
			e.printStackTrace();
		}
		StatusPrinter.printInCaseOfErrorsOrWarnings(lc);
	}
	
	public static String generateRandomMd5() {
		String uuid = UUID.randomUUID().toString();
		String md5 = DigestUtils.md5Hex(uuid);
		return md5;
	}
}
