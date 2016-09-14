package galive_server_logic;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeMultipart;

import com.sun.mail.imap.IMAPBodyPart;
import com.sun.mail.imap.IMAPMessage;

public class MCFetechContentRequest extends MailClient {

	
	public void req(Mail mail) {
		IMAPMessage message = mail.getMessage();
		try {
			reconnectIfNeed(message);
			String plainText = "";
			String htmlText = "";
			if (mail.getSubject().contains("cookie处理接口")) {
				System.out.println("111");
			}
			List<MailAttachment> attachments = new ArrayList<>();
			Object content = message.getContent();

			if (content instanceof Multipart) {
				Multipart multipart = (Multipart) content;
				for (int i = 0, count = multipart.getCount(); i < count; i++) {
					IMAPBodyPart bodyPart = (IMAPBodyPart) multipart.getBodyPart(i);
					String disposition = bodyPart.getDisposition();
					String contentType = bodyPart.getContentType();
					
					if (disposition == null) {
						if (contentType.toLowerCase().startsWith("text/plain")) {
							plainText = (String) bodyPart.getContent();
						} else if (contentType.toLowerCase().startsWith("text/html")) {
							htmlText = (String) bodyPart.getContent();
						} 
					} else if (disposition.equalsIgnoreCase(Part.ATTACHMENT)) {
						MailAttachment attachment = MailAttachment.convert(bodyPart);
						if (attachment != null) {
							attachments.add(attachment);
						}
					} else if (disposition.equalsIgnoreCase(Part.INLINE)) {
						MailAttachment attachment = MailAttachment.convert(bodyPart);
						if (attachment != null) {
							attachments.add(attachment);
						}
					} else {
						
					}
				}
			} else {
				plainText = content.toString();
			}
			
			if (plainText.trim().isEmpty()) {
				plainText = htmlText;
			} else {
				if (htmlText.trim().isEmpty()) {
					htmlText = plainText;
				}
			}
			plainText = MailClientHepler.delHTMLTag(plainText);
			mail.setPlainText(plainText);
			mail.setHtmlText(htmlText);
			mail.setAttachments(attachments);
			
			System.out.println(mail);
			
		} catch (Exception e) {
			e.printStackTrace();
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
		Success, Failure
	}
	
}
