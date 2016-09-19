package com.galive.logic.config;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.galive.logic.helper.LogicHelper;

public class ApplicationConfig {

	private static Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);

	private static ApplicationConfig instance = null;
	
	private SocketConfig socketConfig;
	private LogicConfig logicConfig;
	private PlatformConfig platformConfig;

	public final static ApplicationConfig getInstance() {
		if (instance == null) {
			synchronized (ApplicationConfig.class) {
				if (instance == null) {
					instance = ApplicationConfig.loadConfig();
				}
			}
		}
		return instance;
	}

	private static ApplicationConfig loadConfig() {
		ApplicationConfig sc = new ApplicationConfig();
		InputStream in = null;
		try {
			in = LogicHelper.loadConfig();
			if (in == null) {
				logger.error("读取配置文件失败: 文件不存在。");
				return sc;
			}
			logger.info("加载配置文件...");
			SAXReader reader = new SAXReader();
			Document doc = reader.read(in);
			Element node = doc.getRootElement();

			logger.info("--LogicConfig--");
			LogicConfig logicConfig = new LogicConfig();
			
			sc.setLogicConfig(logicConfig);

			logger.info("--SocketConfig--");
			SocketConfig socketConfig = new SocketConfig();
			Element socketNode = node.element("Socket");
			Element socketUrlNode = socketNode.element("Host");
			String socketUrl = socketUrlNode.getStringValue();
			socketConfig.setHost(socketUrl);
			logger.info("socketUrl:" + socketUrl);

			Element socketPortNode = socketNode.element("Port");
			int socketPort = NumberUtils.toInt(socketPortNode.getStringValue(), 52194);
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

			Element paramsDelimiterNode = socketNode.element("ParamsDelimiter");
			String paramsDelimiter = paramsDelimiterNode.getStringValue();
			socketConfig.setParamsDelimiter(paramsDelimiter);
			logger.info("paramsDelimiter:" + paramsDelimiter);
			
			Element messageDelimiterNode = socketNode.element("MessageDelimiter");
			String messageDelimiter = messageDelimiterNode.getStringValue();
			socketConfig.setMessageDelimiter(messageDelimiter);
			logger.info("messageDelimiter:" + messageDelimiter);
			
			Element heartBeatIntervalNode = socketNode.element("HeartBeatInterval");
			int heartBeatInterval = NumberUtils.toInt(heartBeatIntervalNode.getStringValue(), 20);
			socketConfig.setHeartBeatInterval(heartBeatInterval);
			logger.info("heartBeatInterval:" + heartBeatInterval);

			Element tokenExpireNode = socketNode.element("TokenExpire");
			String tokenExpire = tokenExpireNode.getStringValue();
			socketConfig.setTokenExpire(NumberUtils.toInt(tokenExpire, 7200));
			logger.info("tokenExpire:" + tokenExpire);
			
			sc.setSocketConfig(socketConfig);
			
			logger.info("--PlatformConfig--");
			PlatformConfig platformConfig = new PlatformConfig();
			Element platformNode = node.element("Platform");
			
			Element qqNode = platformNode.element("QQ");
			Element qqAppIdNode = qqNode.element("AppId");
			String qqAppId = qqAppIdNode.getStringValue();
			platformConfig.setQq_appid(qqAppId);
			logger.info("qqAppId:" + qqAppId);
			
			Element wechatNode = platformNode.element("Wechat");
			Element wechatAppIdNode = wechatNode.element("AppId");
			String wechatAppId = wechatAppIdNode.getStringValue();
			platformConfig.setWechat_appid(wechatAppId);
			logger.info("wechatAppId:" + wechatAppId);
			
			Element wechatAppSecretNode = wechatNode.element("AppSecret");
			String wechatAppSecret = wechatAppSecretNode.getStringValue();
			platformConfig.setWechat_appsecret(wechatAppSecret);
			logger.info("wechatAppSecret:" + wechatAppSecret);
			
			sc.setPlatformConfig(platformConfig);
			
			logger.info("配置文件加载成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("读取配置文件失败:" + e.getLocalizedMessage());
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sc;
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

	public PlatformConfig getPlatformConfig() {
		return platformConfig;
	}

	public void setPlatformConfig(PlatformConfig platformConfig) {
		this.platformConfig = platformConfig;
	}
}
