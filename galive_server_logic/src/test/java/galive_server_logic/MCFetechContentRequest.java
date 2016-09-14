package galive_server_logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.Address;
import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.search.MessageIDTerm;
import javax.mail.search.SearchTerm;

import org.apache.commons.lang.ArrayUtils;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPMessage;


public class MCFetechContentRequest extends MailClient {

	public void req(Mail mail) {
		FetechHeaderListResponse resp = new FetechHeaderListResponse();
		try {
			connect();

			IMAPFolder folder = (IMAPFolder) store.getFolder(mail.getFolder());
			folder.open(Folder.READ_ONLY);
			Message message = folder.getMessageByUID(mail.getUid());
			System.out.println("读取content|" + message.getSubject());

			
		} catch (Exception e) {
			
		}
	}
	
	public interface FetechHeaderListListener {
		void onResult(FetechHeaderListResponse resp);
	}
	
	public class FetechHeaderListResponse {
		public FetechHeaderListResult result;
		public String msg;
		public List<Mail> mails = new ArrayList<>();
	}
	
	public enum FetechHeaderListResult {
		Success,
        Failure,
        None,
        NoMore
	}
	
}
