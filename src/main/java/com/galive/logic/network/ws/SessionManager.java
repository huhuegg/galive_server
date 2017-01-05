package com.galive.logic.network.ws;

import com.galive.logic.config.ApplicationConfig;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {

	private final Map<String, Session> sessions = new ConcurrentHashMap<>();
	
	private static SessionManager instance = null;

	public static SessionManager getInstance() {
		if (instance == null) {
			instance = new SessionManager();
		}
		return instance;
	}
	
	public Session findSession(String account) {
		if (!StringUtils.isEmpty(account)) {
			return sessions.get(account);
		}
		return null;
	}
	
	public void addSession(String account, Session session) {
		if (!StringUtils.isEmpty(account)) {
			sessions.put(account, session);
		}
	}
	
	private void removeSession(String account) {
		sessions.remove(account);
	}
	
	void closeAndRemoveSession(String account) {
		removeSession(account);
	}

	public boolean isOnline(String account) {
		Session session = findSession(account);
		return session != null && session.isOpen();
	}
	
	public void sendMessage(String account, String message) {
		if (!StringUtils.isEmpty(message)) {
			Session session = findSession(account);
			if (session != null && session.isOpen()) {
				try {
					session.getRemote().sendString(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void sendMessage(Session session, String message) {
		if (!StringUtils.isEmpty(message)) {
			if (session != null && session.isOpen()) {
				try {
					session.getRemote().sendString(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
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
