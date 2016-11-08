package com.galive.logic.service;

import java.util.List;

import com.galive.logic.exception.LogicException;

public interface RoomService {
	
	/**
	 * 保存媒体服务器房间
	 * @param serverIp
	 * @param serverPort
	 * @param rooms
	 */
	public void saveRooms(String serverIp, int serverPort, List<String> rooms);
	
	/**
	 * 
	 * @return 使用空闲房间
	 * @throws LogicException
	 */
	public String useFreeRoom() throws LogicException;
	
	/**
	 * 将房间置为空闲
	 * @param room
	 */
	public void  returnUsedRoom(String room);
	
	/**
	 * 已使用的媒体服务器房间
	 * @return
	 */
	public List<String> listUsedRoom();
}
	