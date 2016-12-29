package com.galive.logic.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
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
	
	public static InputStream loadProperties() throws IOException {
		String name = SEPARATOR + ApplicationMain.mode.name + SEPARATOR + "jdbc.properties";
		InputStream in = LogicHelper.class.getResourceAsStream(name);
		return in;
	}
	
	public static InputStream loadConfig() throws IOException {
		String name = SEPARATOR + ApplicationMain.mode.name + SEPARATOR + "config.xml";
		return LogicHelper.class.getResourceAsStream(name);
	}
	
	public static void resetLogConfigPath() {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		JoranConfigurator configurator = new JoranConfigurator();
		configurator.setContext(lc);
		lc.reset();
		String name = SEPARATOR + ApplicationMain.mode.name + SEPARATOR + "logback.xml";
		InputStream in = LogicHelper.class.getResourceAsStream(name);
		try {
			configurator.doConfigure(in);
		} catch (JoranException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		StatusPrinter.printInCaseOfErrorsOrWarnings(lc);
	}
	
	public static String generateRandomMd5() {
		String uuid = UUID.randomUUID().toString();
		return DigestUtils.md5Hex(uuid);
	}
	
	private static String makeRandomMeetingName() {
		Random r = new Random();
		long randomLong = r.nextInt(10000000);
		String str = randomLong + "";
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < 8 - str.length(); i++) {
			sb.append("0");
		}
		sb.append(str);
		str = sb.toString();
		return str;
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			String s = LogicHelper.makeRandomMeetingName();
			System.out.println(s);
		}
		
	}
	
}













