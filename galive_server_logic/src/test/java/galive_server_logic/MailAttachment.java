package galive_server_logic;

import javax.mail.internet.MimeUtility;

import com.sun.mail.imap.IMAPBodyPart;

public class MailAttachment {

	private String name = "";
	
	private int size = 0;
	
	private IMAPBodyPart part;
	
	@Override
	public String toString() {
		return name + " " + size;
	}
	
	public static MailAttachment convert(IMAPBodyPart part) {
		MailAttachment attachment = null;
		try {
			attachment = new MailAttachment();
			attachment.setName(MimeUtility.decodeText(part.getFileName()));
			attachment.setSize(part.getSize());
			attachment.setPart(part);
		} catch (Exception e) {
			
		}
		return attachment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public IMAPBodyPart getPart() {
		return part;
	}

	public void setPart(IMAPBodyPart part) {
		this.part = part;
	}
	
}
