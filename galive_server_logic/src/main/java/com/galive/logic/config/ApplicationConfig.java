package com.galive.logic.config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galive.logic.config.RTCConfig.IceServer;
import com.galive.logic.helper.LogicHelper;

public class ApplicationConfig {

	private static Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);

	/**
	 * 重载时间
	 */
	private static long RELOAD_TIMESTAMP = 0;

	/**
	 * 重载间隔
	 */
	private static final long RELOAD_INTERVAL = 1000 * 60 * 3;
	private static ApplicationConfig instance = null;

	private RTCConfig rtcConfig;
	private int tokenExpire = 7200;
	private SocketConfig socketConfig;
	private LogicConfig logicConfig;

	public static ApplicationConfig getInstance() {
		if (instance == null) {
			instance = loadConfig();
		}
		final long NOW = System.currentTimeMillis();
		if (NOW - RELOAD_TIMESTAMP > RELOAD_INTERVAL) {
			RELOAD_TIMESTAMP = NOW;
			new Thread(new Runnable() {

				@Override
				public void run() {
					instance = loadConfig();
				}
			}).start();
		}
		return instance;
	}

	private static ApplicationConfig loadConfig() {
		ApplicationConfig sc = new ApplicationConfig();
		try {
			InputStream in = LogicHelper.loadConfig();
			if (in == null) {
				logger.error("读取配置文件失败: 文件不存在。");
				return sc;
			}
			logger.info("加载配置文件...");
			SAXReader reader = new SAXReader();
			Document doc = reader.read(in);
			Element node = doc.getRootElement();

			Element tokenExpireNode = node.element("TokenExpire");
			String tokenExpire = tokenExpireNode.getStringValue();
			sc.setTokenExpire(Integer.parseInt(tokenExpire));
			logger.info("TokenExpire:" + tokenExpire);

			logger.info("--RtcConfig--");
			RTCConfig rtcConfig = new RTCConfig();
			Element rtcNode = node.element("WebRTC");
			List<IceServer> iceServers = new ArrayList<>();
			Element iceNode = rtcNode.element("IceServer");
			List<?> iceNodes = iceNode.elements();
			for (Object e : iceNodes) {
				IceServer ice = new IceServer();
				Element urlNode = (Element) e;
				String url = urlNode.getText();
				ice.url = url;
				iceServers.add(ice);
				logger.info("------IceServer------" + url);
			}
			rtcConfig.setIceServers(iceServers);
			Element turnUrlNode = rtcNode.element("TurnUrl");
			String turnUrl = turnUrlNode.getText();
			rtcConfig.setTurnUrl(turnUrl);
			logger.info("----TurnUrl----" + turnUrl);
			sc.setRtcConfig(rtcConfig);

			logger.info("--LogicConfig--");
			LogicConfig logicConfig = new LogicConfig();
			Element logicNode = node.element("Logic");
			Element roomMaxUserNode = logicNode.element("RoomMaxUser");
			short roomMaxUser = Short.parseShort(roomMaxUserNode.getStringValue());
			logicConfig.setRoomMaxUser(roomMaxUser);
			logger.info("roomMaxUser:" + roomMaxUser);
			sc.setLogicConfig(logicConfig);

			logger.info("--SocketConfig--");
			SocketConfig socketConfig = new SocketConfig();
			Element socketNode = node.element("Socket");
			Element socketUrlNode = socketNode.element("Host");
			String socketUrl = socketUrlNode.getStringValue();
			socketConfig.setHost(socketUrl);
			logger.info("socketUrl:" + socketUrl);

			Element socketPortNode = socketNode.element("Port");
			int socketPort = Integer.parseInt(socketPortNode.getStringValue());
			socketConfig.setPort(socketPort);
			logger.info("socketPort:" + socketPort);

			Element liveReqNode = socketNode.element("LiveReq");
			String liveReq = liveReqNode.getStringValue();
			socketConfig.setLiveReq(liveReq);
			logger.info("setLiveReq:" + liveReq);

			Element liveRespNode = socketNode.element("LiveResp");
			String liveResp = liveRespNode.getStringValue();
			socketConfig.setLiveResp(liveResp);
			logger.info("setLiveResp:" + liveResp);

			Element delimiterNode = socketNode.element("Delimiter");
			String delimiter = delimiterNode.getStringValue();
			socketConfig.setDelimiter(delimiter);
			logger.info("delimiter:" + delimiter);

			sc.setSocketConfig(socketConfig);
			logger.info("配置文件加载成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("读取配置文件失败:" + e.getLocalizedMessage());
		}
		return sc;
	}

	public RTCConfig getRtcConfig() {
		return rtcConfig;
	}

	public void setRtcConfig(RTCConfig rtcConfig) {
		this.rtcConfig = rtcConfig;
	}

	public int getTokenExpire() {
		return tokenExpire;
	}

	public void setTokenExpire(int tokenExpire) {
		this.tokenExpire = tokenExpire;
	}

	public LogicConfig getLogicConfig() {
		return logicConfig;
	}

	public void setLogicConfig(LogicConfig logicConfig) {
		this.logicConfig = logicConfig;
	}

	public SocketConfig getSocketConfig() {
		return socketConfig;
	}

	public void setSocketConfig(SocketConfig socketConfig) {
		this.socketConfig = socketConfig;
	}
}
