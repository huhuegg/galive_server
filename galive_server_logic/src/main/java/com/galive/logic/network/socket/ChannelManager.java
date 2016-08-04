package com.galive.logic.network.socket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.galive.logic.config.ApplicationConfig;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

public class ChannelManager {

	private Map<String, ChannelHandlerContext> clientChannels = new ConcurrentHashMap<>();
	
	public static final AttributeKey<String> ACCOUNT_KEY = AttributeKey.valueOf("account"); 
	
	private static ChannelManager instance = null;
	
	private String delimiter = "";
	
	public static ChannelManager getInstance() {
		if (instance == null) {
			instance = new ChannelManager();
			instance.delimiter = ApplicationConfig.getInstance().getSocketConfig().getMessageDelimiter();
		}
		return instance;
	}
	
	public ChannelHandlerContext findChannel(String account, String channel) {
		return clientChannels.get(accountTag(account, channel));
	}
	
	public void addChannel(String account, String channel, ChannelHandlerContext context) {
		String accountTag = accountTag(account, channel);
		context.attr(ACCOUNT_KEY).set(accountTag);
		clientChannels.put(accountTag, context);
	}
	
	public void removeChannel(String account, String channel) {
		clientChannels.remove(accountTag(account, channel));
	}
	
	public void closeAndRemoveChannel(String account, String channel) {
		ChannelHandlerContext context = clientChannels.get(accountTag(account, channel));
		closeChannel(context);
		clientChannels.remove(accountTag(account, channel));
	}
	
	public long channelCount() {
		return clientChannels.size();
	}
	
	public void sendMessage(String account, String channel, String message) {
		if (message != null) {
			ChannelHandlerContext context = clientChannels.get(accountTag(account, channel));
			if (channel != null && context.channel().isActive()) {
				message += delimiter;
				context.writeAndFlush(message);
			}
		}
	}
	
	public static void closeChannel(ChannelHandlerContext ctx) {
		if (ctx != null) {
			ctx.flush();
			ctx.close();
		}
	}
	
	private String accountTag(String account, String channel) {
		return account + "#" + channel;
	}
	
	public static String[] getAccount(ChannelHandlerContext context) {
		String accountTag = context.attr(ChannelManager.ACCOUNT_KEY).get(); 
		String account[] = accountTag.split("#");
		return account;
	}
}
