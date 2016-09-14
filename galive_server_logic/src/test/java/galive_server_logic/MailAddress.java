package galive_server_logic;

public class MailAddress {

	private String displayName = "";
	
	private String mailbox = "";
	
	@Override
	public String toString() {
		return displayName + " " + mailbox;
	}
	
	public MailAddress(String displayName, String mailbox) {
		this.displayName = displayName;
		this.mailbox = mailbox;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getMailbox() {
		return mailbox;
	}

	public void setMailbox(String mailbox) {
		this.mailbox = mailbox;
	}
	
	
	
	
}
