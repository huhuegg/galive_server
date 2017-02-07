package com.galive.logic.service;

import com.galive.logic.exception.LogicException;
import org.eclipse.jetty.websocket.api.Session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public interface RemoteClientService {

	public static final String PUBLISH_URL = "rtmp://222.73.196.99/hls";

	public static final String PULL_URL = "rtmp://222.73.196.99/hls/live";

	public static final Map<String, Session> sessions = new ConcurrentHashMap<>();

	Map<String, Object> register(String clientId) throws LogicException;

	void bind(String clientId, String publishUrl) throws LogicException;

	Map<String, Object> startRecord(String clientId) throws LogicException;

	Map<String, Object> stopRecord(String clientId) throws LogicException;

	void unbound(String clientId) throws LogicException;
}
