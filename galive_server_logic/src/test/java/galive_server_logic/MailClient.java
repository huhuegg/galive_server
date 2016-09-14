package galive_server_logic;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.Store;
import galive_server_logic.MCFetechHeadersRequest.MCFetechHeadersListener;
import galive_server_logic.MCFetechHeadersRequest.MCFetechHeadersResponse;

public abstract class MailClient {

	public static final int kStartIndex = 1;
	public static final int kPageSize = 20;

	public MailClient() {
		username = "52119944@qq.com";
		password = "Xmn871001";
		host = "imap.qq.com";
		Properties props = new Properties();
		props.setProperty("mail.store.protocol", "imaps");
		// props.setProperty("mail.imap.host", "imap.qq.com");
		// props.setProperty("mail.imap.port", "993");
		session = Session.getInstance(props);
		session.setDebug(true);
	}

	private Session session;
	protected Store store;
	private String username;
	private String password;
	private String host;

	protected void connect() throws Exception {
		store = session.getStore("imaps");
		store.connect(host, username, password);
	}

	

	
	
	

	public static void main(String[] args) {
		// MailClient.getInstance().reqFolderList();
		new MCFetechHeadersRequest().req("INBOX", kStartIndex, new MCFetechHeadersListener() {
			
			@Override
			public void onResp(MCFetechHeadersResponse resp) {
				System.out.println(resp.result + "|" + resp.mails.size());
				for (Mail mail : resp.mails) {
					if (mail.isHasAttachments()) {
						new MCFetechContentRequest().req(mail);
					}
				}
			}
		});

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

	
	

}
