package galive_server_logic;

import javax.mail.Folder;
import javax.mail.Store;

public class MCFetechFoldersRequest extends MailClient {

	public void reqFolderList() {
		try {
			Store store = connect();
			Folder[] folders = store.getDefaultFolder().list();
			for (Folder folder : folders) {
				System.out.println(">> " + folder.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
