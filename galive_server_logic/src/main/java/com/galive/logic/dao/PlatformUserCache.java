package com.galive.logic.dao;

import java.util.List;
import java.util.Set;

public interface PlatformUserCache {

	public void saveSharedDeviceid(String deviceid, String udid);
	
	public void removeSharedDeviceid(String deviceid, String udid);
	
	public void saveContact(String deviceid, String contactDeviceid);
	
	public Set<String> listSharedDeviceids(String deviceid);
	
	public List<String> listContacts(String deviceid, int start, int end);
}
