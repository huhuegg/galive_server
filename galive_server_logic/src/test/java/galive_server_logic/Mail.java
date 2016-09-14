package galive_server_logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sun.mail.imap.IMAPMessage;

public class Mail {

	private String folder;
	
	private long uid = 0;
	
	private String messageID = "";
	
	private String subject = "";
	
	private MailAddress from;
	
	private List<MailAddress> to = new ArrayList<>();
	
	private List<MailAddress> cc = new ArrayList<>();
	
	private List<MailAddress> bcc = new ArrayList<>();
	
	private boolean hasAttachments = false;
	
	private Date sentDate = new Date();
	
	private List<MailAttachment> attachments = new ArrayList<>();
	
	private String plainText = "";
	
	private String htmlText = "";
	
	private IMAPMessage message;
	
	@Override
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("===================================");
		b.append("\n");
		b.append("folder|" + folder);
		b.append("\n");
		b.append("uid|" + uid);
		b.append("\n");
		b.append("messageID|" + messageID);
		b.append("\n");
		b.append("sentDate|" + sentDate);
		b.append("\n");
		b.append("subject|" + subject);
		b.append("\n");
/*		b.append("plainText|" + plainText);
		b.append("\n");*/
/*		b.append("htmlText|" + htmlText);
		b.append("\n");*/
		b.append("from|" + from.toString());
		b.append("\n");
		b.append("to|");
		for (MailAddress address : to) {
			b.append(address.toString() + "|");
		}
		b.append("\n");
		b.append("cc|");
		for (MailAddress address : cc) {
			b.append(address.toString() + "|");
		}
		b.append("\n");
		b.append("bcc|");
		for (MailAddress address : bcc) {
			b.append(address.toString() + "|");
		}
		b.append("\n");
		b.append("hasAttachments|" + hasAttachments);
		b.append("\n");
		b.append("attachments|");
		for (MailAttachment attachment : attachments) {
			b.append(attachment.toString() + "|");
		}
		b.append("\n");
		
		
		b.append("===================================");
		return b.toString();
	}

	public String getMessageID() {
		return messageID;
	}

	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public MailAddress getFrom() {
		return from;
	}

	public void setFrom(MailAddress from) {
		this.from = from;
	}

	public List<MailAddress> getTo() {
		return to;
	}

	public void setTo(List<MailAddress> to) {
		this.to = to;
	}

	public List<MailAddress> getCc() {
		return cc;
	}

	public void setCc(List<MailAddress> cc) {
		this.cc = cc;
	}

	public List<MailAddress> getBcc() {
		return bcc;
	}

	public void setBcc(List<MailAddress> bcc) {
		this.bcc = bcc;
	}

	public boolean isHasAttachments() {
		return hasAttachments;
	}

	public void setHasAttachments(boolean hasAttachments) {
		this.hasAttachments = hasAttachments;
	}

	public Date getSentDate() {
		return sentDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public IMAPMessage getMessage() {
		return message;
	}

	public void setMessage(IMAPMessage message) {
		this.message = message;
	}

	public List<MailAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<MailAttachment> attachments) {
		this.attachments = attachments;
	}

	public String getPlainText() {
		return plainText;
	}

	public void setPlainText(String plainText) {
		this.plainText = plainText;
	}

	public String getHtmlText() {
		return htmlText;
	}

	public void setHtmlText(String htmlText) {
		this.htmlText = htmlText;
	}
	
	
	
}
