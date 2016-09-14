package galive_server_logic;

import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	public static String delHTMLTag(String htmlStr) {
		String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
		String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
		String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

		Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll(""); // 过滤script标签

		Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll(""); // 过滤style标签

		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll(""); // 过滤html标签

		return htmlStr.trim(); // 返回文本字符串
	}

}
