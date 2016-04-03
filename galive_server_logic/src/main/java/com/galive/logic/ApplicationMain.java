package com.galive.logic;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.dao.cache.RedisManager;
import com.galive.logic.helper.AnnotationManager;
import com.galive.logic.log.LogManager;
import com.galive.logic.model.Sid;
import com.galive.logic.model.Sid.EntitySeq;
import com.galive.logic.network.http.jetty.JettyServer;
import com.galive.logic.network.socket.netty.NettyServer;

import redis.clients.jedis.Jedis;

public class ApplicationMain implements Daemon {

	private static Logger logger = LoggerFactory.getLogger(ApplicationMain.class);

	private static ApplicationMain instance;

	private ApplicationMain() {
	}

	public static synchronized ApplicationMain getInstance() {
		if (instance == null) {
			instance = new ApplicationMain();
		}
		return instance;
	}

	public enum ApplicationMode {
		Develop("develop"), Distribution("distribution");

		public String name;

		private ApplicationMode(String name) {
			this.name = name;
		}
	}

	private JettyServer jettyServer;
	private NettyServer nettyServer;
	private ApplicationMode mode = ApplicationMode.Develop;

	public static void main(String[] args) {
		ApplicationMain app = ApplicationMain.getInstance();
		app.initParams(args);
		try {
			app.start();
			app.addShutdownHook();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("LogicServer启动失败:" + e.getMessage());
			try {
				app.stop();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	public void init(DaemonContext context) throws DaemonInitException, Exception {
		String attrs[] = context.getArguments();
		ApplicationMain app = ApplicationMain.getInstance();
		app.initParams(attrs);
	}

	public void start() throws Exception {
		logger.info("【读取配置】");
		try {
			logger.info("加载配置文件");
			ApplicationConfig.getInstance();
			
			logger.info("重载log配置");
			LogManager.resetLogConfigPath();
			
			logger.info("加载Handler标签");
			AnnotationManager.initAnnotation();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("初始化失败:" + e.getMessage());
			throw new Exception("初始化失败:" + e.getMessage());
		}

		logger.info("【测试数据库连接】");
		try {
			long seq = Sid.getNextSequence(EntitySeq.Test);
			logger.info("mongo test seq:" + seq + ",连接成功");
			
			Jedis j = RedisManager.getResource();
			j.set("test", System.currentTimeMillis() + "");
			logger.info("redis test value:" + j.get("test") + ",连接成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("数据库连接失败:" + e.getMessage());
			throw new Exception("数据库连接失败:" + e.getMessage());
		} 

		logger.info("【绑定http服务】");
		try {
			logger.info("启动jetty...");
			jettyServer = new JettyServer();
			jettyServer.start();
			logger.info("jetty启动成功。");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("jetty启动失败:" + e.getMessage());
			throw new Exception("jetty启动失败:" + e.getMessage());
		}
		
		logger.info("【绑定socket服务】");
		try {
			logger.info("启动netty...");
			nettyServer = new NettyServer();
			nettyServer.start();
			logger.info("netty启动成功。");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("netty启动失败:" + e.getMessage());
			throw new Exception("netty启动失败:" + e.getMessage());
		}
		logger.info("===============================================");
		logger.info("==   *************************************   ==");
		logger.info("==   *****                           *****   ==");
		logger.info("==   *****   Logic Server Started    *****   ==");
		logger.info("==   *****                           *****   ==");
		logger.info("==   *************************************   ==");
		logger.info("===============================================");
	}

	public void stop() throws Exception {
		stopServer();
	}

	public void destroy() {
		stopServer();
	}

	private void stopServer() {
		try {
			if (jettyServer != null) {
				jettyServer.stop();
			}
			if (nettyServer != null) {
				nettyServer.stop();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initParams(String params[]) {
		ApplicationMode mode = ApplicationMode.Develop;
		if (!ArrayUtils.isEmpty(params)) {
			String arg = params[0];
			if (arg.equals("1")) {
				mode = ApplicationMode.Distribution;
			}
		}
		ApplicationMain.getInstance().setMode(mode);
	}

	private void addShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				stopServer();
			}
		});
	}

	public void setMode(ApplicationMode mode) {
		this.mode = mode;
	}

	public ApplicationMode getMode() {
		return mode;
	}

}
