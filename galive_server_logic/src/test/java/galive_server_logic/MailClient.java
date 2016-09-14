package galive_server_logic;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.Store;

import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.imap.IMAPStore;

import galive_server_logic.MCFetechHeadersRequest.MCFetechHeadersListener;
import galive_server_logic.MCFetechHeadersRequest.MCFetechHeadersResponse;

public abstract class MailClient {

	public MailClient() {
		username = "52119944@qq.com";
		password = "Xmn871001";
		host = "imap.qq.com";
	}

	private String username;
	private String password;
	private String host;
	private Store store;
	private Session session;
	
	protected Store connect() throws Exception {
		Properties props = new Properties();
		// props.setProperty("mail.imap.host", "imap.qq.com");
//		 props.setProperty("mail.imap.port", "143");
		props.setProperty("mail.store.protocol", "imaps");
		session = Session.getInstance(props);
//		session.setDebug(true);
		store = session.getStore("imaps");
		store.connect(host, username, password);
		return store;
	}

	protected void reconnectIfNeed(IMAPMessage message) throws Exception {
		IMAPStore store = (IMAPStore) message.getSession().getStore();
		if (!store.isConnected()) {
			store.connect(host, "nextcont@foxmail.com", "xhdmuekhsdvshiha");
		}
		Folder folder = message.getFolder();
		if (!folder.isOpen()) {
			folder.open(Folder.READ_ONLY);
		}
	}
	

	
	
	

	public static void main(String[] args) {
		// MailClient.getInstance().reqFolderList();
		new MCFetechHeadersRequest().req("INBOX", 1, new MCFetechHeadersListener() {
			
			@Override
			public void onResp(MCFetechHeadersResponse resp) {
				System.out.println(resp.result + "|" + resp.mails.size());
				for (Mail mail : resp.mails) {
					new MCFetechContentRequest().req(mail);
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
