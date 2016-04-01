package com.galive.logic;

import java.util.Properties;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galive.logic.helper.LogicHelper;

public class JettyServer {
	
	private static Logger logger = LoggerFactory.getLogger(JettyServer.class);
	
	private Server server;
	
	public void start() throws Exception {
		Properties prop = LogicHelper.loadProperties();
		int port = Integer.parseInt(prop.getProperty("http.port", "8080"));
		logger.info("绑定端口:" + port);
		server = new Server(port);
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);
		
		context.addServlet(new ServletHolder(new LogicHttpHandler()), prop.getProperty("http.action.logic", "/logic"));
		server.start();
//		server.join();
	}
	
	public void stop() throws Exception {
		server.stop();
	}
	
}
