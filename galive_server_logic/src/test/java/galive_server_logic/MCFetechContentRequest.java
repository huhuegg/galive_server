package galive_server_logic;

import javax.mail.Folder;

import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.imap.IMAPStore;


public class MCFetechContentRequest extends MailClient {

	public void req(Mail mail) {
		IMAPMessage message = mail.getMessage();
		try {
			reconnectIfNeed(message);
			
			String contentType = message.getContentType();
		
			
			System.out.println("contentType|" + contentType);
			
		} catch (Exception e) {
			
		}
	}
	
	public interface MCFetechContentListener {
		void onResult(MCFetechContentResponse resp);
	}
	
	public class MCFetechContentResponse {
		public MCFetechContentResult result;
		public String msg;
	}
	
	public enum MCFetechContentResult {
		Success,
        Failure
	}
	
}
