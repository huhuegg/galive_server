package com.galive.logic.helper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.UUID;
import org.apache.commons.codec.digest.DigestUtils;
import com.galive.logic.ApplicationMain;

public class LogicHelper {

	public static Properties loadProperties() throws IOException {
		Properties prop = new Properties();
		String name = File.separator + ApplicationMain.getInstance().getMode().name + File.separator + "jdbc.properties";
		InputStream in = LogicHelper.class.getResourceAsStream(name);
		prop.load(in);
		return prop;
	}
	
	public static InputStream loadConfig() throws IOException {
		String name = File.separator + ApplicationMain.getInstance().getMode().name + File.separator + "GALive.xml";
		InputStream in = LogicHelper.class.getResourceAsStream(name);
		return in;
	}
	
	public static String generateRandomMd5() {
		String uuid = UUID.randomUUID().toString();
		String md5 = DigestUtils.md5Hex(uuid);
		return md5;
	}
}
