package galive_server_logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.Address;
import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.Store;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang.ArrayUtils;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPMessage;

public class MCFetechHeadersRequest extends MailClient {

	public static final int kStartIndex = 1;
	public static final int kPageSize = 20;
	
	public void req(String folderName, int index, MCFetechHeadersListener listener) {
		MCFetechHeadersResponse resp = new MCFetechHeadersResponse();
		IMAPFolder folder = null;
		try {
			Store store = connect();
			folder = (IMAPFolder) store.getFolder(folderName);
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
			List<Mail> mails = new ArrayList<>();
			for (Message message : messages) {
				Mail mail = new Mail();
				mail.setFolder(folderName);
				mail.setMessage((IMAPMessage) message);

				IMAPMessage msg = (IMAPMessage) message;

				StringBuffer buffer = new StringBuffer();
				buffer.append("================================\n");

				long uid = folder.getUID(msg);
				buffer.append("UID|" + uid + "\n");
				mail.setUid(uid);

				String messageID = msg.getMessageID();
				buffer.append("MessageID|" + messageID + "\n");
				mail.setMessageID(messageID);

				String subject = MimeUtility.decodeText(msg.getSubject());
				buffer.append("subject|" + subject + "\n");
				mail.setSubject(subject);

				String from = msg.getSender().toString();
				String fromAddress[] = MailClientHepler.formatAddress(from);
				buffer.append("from|" + fromAddress[0] + " " + fromAddress[1] + "\n");
				MailAddress fromMailAddress = new MailAddress(fromAddress[0], fromAddress[1]);
				mail.setFrom(fromMailAddress);

				Address tos[] = msg.getRecipients(RecipientType.TO);
				List<MailAddress> toMailAddresses = new ArrayList<>();
				for (Address to : tos) {
					String toAddress[] = MailClientHepler.formatAddress(to.toString());
					buffer.append("to|" + toAddress[0] + " " + toAddress[1] + "\n");
					MailAddress toMailAddress = new MailAddress(toAddress[0], toAddress[1]);
					toMailAddresses.add(toMailAddress);
				}
				mail.setTo(toMailAddresses);

				Address ccs[] = msg.getRecipients(RecipientType.CC);
				List<MailAddress> ccMailAddresses = new ArrayList<>();
				if (ccs != null) {
					for (Address cc : ccs) {
						String ccAddress[] = MailClientHepler.formatAddress(cc.toString());
						buffer.append("cc|" + ccAddress[0] + " " + ccAddress[1] + "\n");
						MailAddress ccMailAddress = new MailAddress(ccAddress[0], ccAddress[1]);
						ccMailAddresses.add(ccMailAddress);
					}
				}
				mail.setCc(ccMailAddresses);

				Address bccs[] = msg.getRecipients(RecipientType.BCC);
				List<MailAddress> bccMailAddresses = new ArrayList<>();
				if (bccs != null) {
					for (Address bcc : bccs) {
						String bccAddress[] = MailClientHepler.formatAddress(bcc.toString());
						buffer.append("bcc|" + bccAddress[0] + " " + bccAddress[1] + "\n");
						MailAddress bccMailAddress = new MailAddress(bccAddress[0], bccAddress[1]);
						bccMailAddresses.add(bccMailAddress);
					}
				}
				mail.setBcc(bccMailAddresses);

				Date date = msg.getSentDate();
				buffer.append("date|" + date + "\n");
				mail.setSentDate(date);

				String[] hasAtta = msg.getHeader("X-Has-Attach");
				boolean hasAttachments = false;
				if (hasAtta != null) {
					buffer.append("hasAtta|" + hasAtta[0] + "\n");
					hasAttachments = hasAtta[0].equals("yes");
				}
				mail.setHasAttachments(hasAttachments);

				buffer.append("================================\n");

				System.out.println(buffer.toString());

				mails.add(mail);
			}

			if (index == kStartIndex && mails.isEmpty()) {
				resp.result = MCFetechHeadersResult.None;
				listener.onResp(resp);
				return;
			}
			// NoMore
			if (index > kStartIndex && mails.size() < kPageSize) {
				resp.result = MCFetechHeadersResult.NoMore;
				resp.mails = mails;
				listener.onResp(resp);
				return;
			}
			resp.result = MCFetechHeadersResult.Success;
			resp.mails = mails;
			listener.onResp(resp);
		} catch (Exception e) {
			resp.result = MCFetechHeadersResult.Failure;
			resp.msg = e.getLocalizedMessage();
			listener.onResp(resp);
		} finally {
			
		}
	}

	public interface MCFetechHeadersListener {
		void onResp(MCFetechHeadersResponse resp);
	}

	public class MCFetechHeadersResponse {
		public MCFetechHeadersResult result;
		public String msg;
		public List<Mail> mails = new ArrayList<>();
	}

	public enum MCFetechHeadersResult {
		Success, Failure, None, NoMore
	}

}
