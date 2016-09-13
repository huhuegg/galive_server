package galive_server_logic;

import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Message.RecipientType;

import org.apache.commons.lang.ArrayUtils;

import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.imap.IMAPMultipartDataSource;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class MailClient {

	public static final int kStartIndex = 1;
	public static final int kPageSize = 20;

	private static MailClient instance;

	public static synchronized MailClient getInstance() {
		if (instance == null) {
			instance = new MailClient();
		}
		return instance;
	}

	private MailClient() {
		username = "52119944@qq.com";
		password = "Xmn871001";
		host = "imap.qq.com";
		Properties props = new Properties();
		props.setProperty("mail.store.protocol", "imaps");
		// props.setProperty("mail.imap.host", "imap.qq.com");
		// props.setProperty("mail.imap.port", "993");
		session = Session.getInstance(props);
		// session.setDebug(true);
	}

	private Session session;
	private Store store;
	private String username;
	private String password;
	private String host;

	private void connect() throws Exception {
		store = session.getStore("imaps");
		store.connect(host, username, password);
	}

	public void reqFolderList() {
		try {
			connect();
			Folder[] folders = store.getDefaultFolder().list();
			for (Folder folder : folders) {
				System.out.println(">> " + folder.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reqMailList(String folderName, int index) {
		try {
			connect();

			Folder folder = store.getFolder(folderName);
			folder.open(Folder.READ_ONLY);

			int count = folder.getMessageCount();
			int start = 0;
			int end = 0;

			if (count > kPageSize) {
				start = count - index - kPageSize + 2;
				end = start + kPageSize - 1;
			} else {
				start = index;
				end = start + count - 1;
			}

			Message[] messages = folder.getMessages(start, end);
			ArrayUtils.reverse(messages);
			System.out.println("获取邮件数:" + messages.length);

			FetchProfile fp = new FetchProfile();
			fp.add(FetchProfile.Item.ENVELOPE);
			fp.add("Message-ID");

			folder.fetch(messages, fp);

			for (Message message : messages) {
				IMAPMessage msg = (IMAPMessage) message;

				StringBuffer buffer = new StringBuffer();
				buffer.append("================================\n");

				//String messageID = msg.getMessageID();
				String messageID = msg.getHeader("message-id")[0];
				buffer.append("MessageID|" + messageID + "\n");
				
				String subject = msg.getSubject();
				//String subject = msg.getHeader("Subject")[0];
				buffer.append("subject|" + subject + "\n");
				
				String from = msg.getSender().toString();
//				String from = msg.getHeader("From")[0];
				String fromAddress[] = formatAddress(from);
				buffer.append("from|" + fromAddress[0] + " " + fromAddress[1] + "\n");
				
				Address tos[] = msg.getRecipients(RecipientType.TO);
				for (Address to : tos) {
					String toAddress[] = formatAddress(to.toString());
					buffer.append("to|" + toAddress[0] + " " + toAddress[1] + "\n");
				}
				
				Address ccs[] = msg.getRecipients(RecipientType.CC);
				for (Address cc : ccs) {
					String toAddress[] = formatAddress(cc.toString());
					buffer.append("cc|" + toAddress[0] + " " + toAddress[1] + "\n");
				}
				
				Address bccs[] = msg.getRecipients(RecipientType.BCC);
				for (Address bcc : bccs) {
					String toAddress[] = formatAddress(bcc.toString());
					buffer.append("bcc|" + toAddress[0] + " " + toAddress[1] + "\n");
				}
					
				
				
				String date = msg.getHeader("Date")[0];
				buffer.append("date|" + date + "\n");
				
				String[] hasAtta = msg.getHeader("X-Has-Attach");
				if (hasAtta != null) {
					buffer.append("hasAtta|" + hasAtta[0] + "\n");
				}
				

				// String to = msg.getre.toString();
				// String fromAddress[] = formatAddress(from);
				//
				// buffer.append("from|" + fromAddress[0] + " " + fromAddress[1]
				// + "\n");

//				String contentType = message.getContentType();
//				buffer.append("contentType|" + contentType + "\n");

				// Multipart multipart = (Multipart) message.getContent();
				// int attachmentCount = multipart.getCount();
				// buffer.append("attachment count|" + attachmentCount + "\n");
				buffer.append("================================\n");
				
//				Enumeration<Header> headers = msg.getAllHeaders();
//				while (headers.hasMoreElements()) {
//					Header header = (Header) headers.nextElement();
//					System.out.println("header|" + header.getName() + ":" + header.getValue());
//				}

				System.out.println(buffer.toString());

				

				// Address[] from = msg.getFrom();
				// System.out.println(from[0].toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//
	//
	// if (part.isMimeType("text/plain")) {
	// ...
	// } else if (part.isMimeType("multipart/*")) {
	// ...
	// } else if (part.isMimeType("message/rfc822")) {
	// ...
	// } else {
	// ...
	// }

	public static void main(String[] args) {
		// MailClient.getInstance().reqFolderList();
		MailClient.getInstance().reqMailList("INBOX", kStartIndex);

		// Folder inbox = store.getFolder("INBOX");
		// inbox.open(Folder.READ_ONLY);
		// Message msg = inbox.getMessage(inbox.getMessageCount());
		//
		// inbox.get
		//
		// Address[] in = msg.getFrom();
		// for (Address address : in) {
		// System.out.println("FROM:" + address.toString());
		// }
		// Multipart mp = (Multipart) msg.getContent();
		// BodyPart bp = mp.getBodyPart(0);
		// System.out.println("SENT DATE:" + msg.getSentDate());
		// System.out.println("SUBJECT:" + msg.getSubject());
		// System.out.println("CONTENT:" + bp.getContent());
		// } catch (Exception mex) {
		// mex.printStackTrace();
		// }
	}

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
