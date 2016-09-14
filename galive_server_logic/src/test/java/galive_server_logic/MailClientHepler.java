package galive_server_logic;

import java.nio.charset.Charset;

import sun.misc.BASE64Decoder;

public class MailClientHepler {

	public static String[] formatAddress(String addressStr) {
		String displayName = "";
		String mailbox = "";
		String[] str = addressStr.split(" ");
		if (str.length == 1) {
			mailbox = addressStr;
		} else {
			String name = str[0];
			name = name.replace("=?", "");
			name = name.replace("?=", "");
			String[] names = name.split("\\?B\\?");
			if (names.length == 2) {
				String charset = names[0];
				String base64 = names[1];
				try {
					BASE64Decoder decoder = new BASE64Decoder();
					byte[] bytes = decoder.decodeBuffer(base64);
					displayName = new String(bytes, Charset.forName(charset)).trim();
				} catch (Exception e) {

				}
			}
			mailbox = str[1];
		}

		mailbox = mailbox.replace("<", "").replace(">", "");
		return new String[] { displayName, mailbox };
	}
	
}
