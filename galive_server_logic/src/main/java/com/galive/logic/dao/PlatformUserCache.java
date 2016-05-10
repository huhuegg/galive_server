package com.galive.logic.dao;

import java.util.List;

public interface PlatformUserCache {
	
	public void saveContact(String deviceid, String contactDeviceid);
	
	public List<String> listContacts(String deviceid, int start, int end);
}
