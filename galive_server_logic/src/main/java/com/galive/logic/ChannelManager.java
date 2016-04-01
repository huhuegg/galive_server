package com.galive.logic;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

public class ChannelManager {

	private Map<String, ChannelHandlerContext> clientChannels = new ConcurrentHashMap<>();
	
	public static final AttributeKey<String> USER_SID_KEY = AttributeKey.valueOf("userSid"); 
	
	private static ChannelManager instance = null;
	
	public static ChannelManager getInstance() {
		if (instance == null) {
			instance = new ChannelManager();
		}
		return instance;
	}
	
	public void addChannel(String userSid, ChannelHandlerContext channel) {
		channel.attr(USER_SID_KEY).set(userSid);
		clientChannels.put(userSid, channel);
	}
	
	public void removeChannel(String userSid) {
		ChannelHandlerContext channel = clientChannels.get(userSid);
		if (channel != null) {
			channel.flush();
			channel.close();
		}
		clientChannels.remove(userSid);
	}
	
	public long channelCount() {
		return clientChannels.size();
	}
	
	public void sendMessage(String userSid, String message) {
		ChannelHandlerContext channel = clientChannels.get(userSid);
		if (channel != null && channel.channel().isActive()) {
			channel.writeAndFlush(message);
		}
	}
}
