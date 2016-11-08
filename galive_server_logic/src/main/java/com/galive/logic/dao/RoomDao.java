package com.galive.logic.dao;

import java.util.Set;

public interface RoomDao {
	
	/**
	 * 删除所有已使用的
	 */
	public void deleteUseds();
	
	/**
	 * 删除使用的房间
	 * @param room
	 */
	public void deleteUsed(String room);
	
	/**
	 * 移除所有空闲
	 */
	public void deleteFrees();
	
	/**
	 * 保存至空闲房间
	 */
	public void saveFree(String room);
	
	/**
	 * 保存至已使用的
	 */
	public void saveUsed(String room);
	
	/**
	 * 是否使用中
	 * @param room
	 * @return
	 */
	public boolean roomUsed(String room);
	
	/**
	 * 使用空闲房间
	 * @return
	 */
	public String useFree();
	
	/**
	 * 查找所有已使用房间
	 * @return
	 */
	public Set<String> findUseds();
}
