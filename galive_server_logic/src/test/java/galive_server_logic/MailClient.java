package galive_server_logic;

import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;

import com.sun.mail.imap.IMAPMessage;

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
//        props.setProperty("mail.imap.host", "imap.qq.com"); 
//        props.setProperty("mail.imap.port", "993"); 
		session = Session.getInstance(props);
		session.setDebug(true);
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
			for(Folder folder : folders) {
				System.out.println(">> "+folder.getName());
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
			Message[] messages = folder.getMessages(index, index + kPageSize);
			for (Message message : messages) {
				IMAPMessage msg = (IMAPMessage) message;
				Enumeration<Header> headers = msg.getAllHeaders();
				Address[] from = msg.getFrom();
				System.out.println(from[0].toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}   
	}
	
	
	
	
	
	
	
	
	
	

	public static void main(String[] args) {
		MailClient.getInstance().reqFolderList();
		MailClient.getInstance().reqMailList("INBOX", kStartIndex);
     
            
//            Folder inbox = store.getFolder("INBOX");
//            inbox.open(Folder.READ_ONLY);
//            Message msg = inbox.getMessage(inbox.getMessageCount());
//            
//            inbox.get
//            
//            Address[] in = msg.getFrom();
//            for (Address address : in) {
//                System.out.println("FROM:" + address.toString());
//            }
//            Multipart mp = (Multipart) msg.getContent();
//            BodyPart bp = mp.getBodyPart(0);
//            System.out.println("SENT DATE:" + msg.getSentDate());
//            System.out.println("SUBJECT:" + msg.getSubject());
//            System.out.println("CONTENT:" + bp.getContent());
//        } catch (Exception mex) {
//            mex.printStackTrace();
//        }
    }

}
