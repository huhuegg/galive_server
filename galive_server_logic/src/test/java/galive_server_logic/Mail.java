package galive_server_logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	
	
	
}
