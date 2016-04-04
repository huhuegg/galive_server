package com.galive.logic.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;
import org.apache.commons.codec.digest.DigestUtils;
import com.galive.logic.ApplicationMain;

public class LogicHelper {

	private static final String SEPARATOR = "/";
	
	public static Properties loadProperties() throws IOException {
		Properties prop = new Properties();
		String name = SEPARATOR + ApplicationMain.getInstance().getMode().name + SEPARATOR + "jdbc.properties";
		InputStream in = LogicHelper.class.getResourceAsStream(name);
		prop.load(in);
		return prop;
	}
	
	public static InputStream loadConfig() throws IOException {
		String name = SEPARATOR + ApplicationMain.getInstance().getMode().name + SEPARATOR + "config.xml";
		InputStream in = LogicHelper.class.getResourceAsStream(name);
		return in;
	}
	
	public static String loadApnsCertPath(String certName) {
		String name = SEPARATOR + ApplicationMain.getInstance().getMode().name + SEPARATOR + certName;
		String path = LogicHelper.class.getResource(name).getPath();
		return path;
	}
	
	public static String generateRandomMd5() {
		String uuid = UUID.randomUUID().toString();
		String md5 = DigestUtils.md5Hex(uuid);
		return md5;
	}
}
