package com.galive.logic.config;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.galive.logic.ApplicationMain;
import com.galive.logic.ApplicationMain.ApplicationMode;
import com.galive.logic.helper.LogicHelper;

public class ApplicationConfig {

	private static Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);

	private static ApplicationConfig instance = null;
	
	private SocketConfig socketConfig;
	private LogicConfig logicConfig;
	private PlatformConfig platformConfig;
	private FileConfig fileConfig;

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
			Element logicNode = node.element("Logic");
			Element meidaServerHostNode = logicNode.element("MeidaServerHost");
			String meidaServerHost = meidaServerHostNode.getStringValue();
			logicConfig.setMeidaServerHost(meidaServerHost);
			logger.info("meidaServerHost:" + meidaServerHost);
			
			Element meidaServerPortNode = logicNode.element("MeidaServerPort");
			String meidaServerPort = meidaServerPortNode.getStringValue();
			logicConfig.setMeidaServerPort(meidaServerPort);
			logger.info("meidaServerPort:" + meidaServerPort);
			
			sc.setLogicConfig(logicConfig);
			
			logger.info("--FileConfig--");
			FileConfig fileConfig = new FileConfig();
			Element fileNode = node.element("File");
			Element fileHostNode = fileNode.element("Host");
			String fileHost = fileHostNode.getStringValue();
			fileConfig.setHost(fileHost);
			logger.info("fileHost:" + fileHost);
			
			Element filePathNode = fileNode.element("Path");
			String filePath = filePathNode.getStringValue();
			fileConfig.setPath(filePath);
			logger.info("filePath:" + filePath);
			
			Element folderUsrAvatarNode = fileNode.element("FolderUsrAvatar");
			String folderUsrAvatar = folderUsrAvatarNode.getStringValue();
			fileConfig.setFolder_usr_avatar(folderUsrAvatar);
			logger.info("folderUsrAvatar:" + folderUsrAvatar);
			
			Element folderMeetingCoverNode = fileNode.element("FolderMeetiongCover");
			String folderMeetingCover = folderMeetingCoverNode.getStringValue();
			fileConfig.setFolder_meeting_cover(folderMeetingCover);
			logger.info("folderMeetingCover:" + folderMeetingCover);
			
			
			sc.setFileConfig(fileConfig);
			

			logger.info("--SocketConfig--");
			SocketConfig socketConfig = new SocketConfig();
			Element socketNode = node.element("Socket");
			
			if (ApplicationMain.mode == ApplicationMode.Develop) {
				Element socketUrlNode = socketNode.element("Host");
				String socketUrl = socketUrlNode.getStringValue();
				socketConfig.setHost(socketUrl);
				logger.info("socketUrl:" + socketUrl);

				Element socketPortNode = socketNode.element("Port");
				int socketPort = NumberUtils.toInt(socketPortNode.getStringValue(), 52194);
				socketConfig.setPort(socketPort);
				logger.info("socketPort:" + socketPort);
			} else {
				String host = System.getenv().get("GALIVE_SOCKET_HOST");
				int port = Integer.parseInt(System.getenv().get("GALIVE_SOCKET_PORT"));
				socketConfig.setHost(host);
				socketConfig.setPort(port);
				logger.info("socketHost:" + host);
				logger.info("socketPort:" + port);
				
				sc.getFileConfig().setPath(System.getenv().get("GALIVE_FILE_PATH"));
			}
			
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

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		ApplicationConfig.logger = logger;
	}

	public FileConfig getFileConfig() {
		return fileConfig;
	}

	public void setFileConfig(FileConfig fileConfig) {
		this.fileConfig = fileConfig;
	}

	public static void setInstance(ApplicationConfig instance) {
		ApplicationConfig.instance = instance;
	}
}
