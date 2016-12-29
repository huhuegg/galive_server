package com.galive.logic.network.socket;

import com.galive.logic.config.ApplicationConfig;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.commons.lang.StringUtils;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelManager {

	//private Map<String, ChannelHandlerContext> clientChannels = new ConcurrentHashMap<>();
	
	private final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	private final Map<String, ChannelId> channelIds = new ConcurrentHashMap<>();
	
//	public static final AttributeKey<String> ACCOUNT_KEY = AttributeKey.valueOf("account");
	
	private static ChannelManager instance = null;
	
	private String delimiter = "";

	static final AttributeKey<String> ACCOUNT_KEY = AttributeKey.valueOf("account");


	public static ChannelManager getInstance() {
		if (instance == null) {
			instance = new ChannelManager();
			instance.delimiter = ApplicationConfig.getInstance().getSocketConfig().getMessageDelimiter();
		}
		return instance;
	}
	
	public Channel findChannel(String account) {
		if (!StringUtils.isEmpty(account)) {
			ChannelId channelId = channelIds.get(account);
			if (channelId != null) {
				return channels.find(channelId);
			}
		}
		return null;
	}
	
	public void addChannel(String account, Channel channel) {
		channel.attr(ACCOUNT_KEY).set(account);
		channels.add(channel);
		ChannelId channelId = channel.id();
		channelIds.put(account, channelId);
	}
	
	private void removeChannel(String account) {
		Channel channel = findChannel(account);
		if (channel != null) {
			channels.remove(channel);
			channelIds.remove(account);
		}
	}
	
	void closeAndRemoveChannel(String account) {
		Channel channel = findChannel(account);
		if (channel != null) {
			closeChannel(channel);
		}
		removeChannel(account);
	}

	public boolean isOnline(String account) {
		Channel channel = findChannel(account);
		return channel != null && channel.isActive();
	}
	
	public void sendMessage(String account, String message) {
		if (!StringUtils.isEmpty(message)) {
			Channel channel = findChannel(account);
			if (channel != null && channel.isActive()) {
				message += delimiter;
				channel.writeAndFlush(message);
			}
		}
	}
	
	static void closeChannel(Channel channel) {
		if (channel != null) {
			channel.flush();
			channel.close();
		}
	}

}
