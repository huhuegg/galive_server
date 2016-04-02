package com.galive.logic.network.http.jetty;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.galive.logic.network.http.LogicServlet;

public class JettyServer {
	
	private static Logger logger = LoggerFactory.getLogger(JettyServer.class);
	
	private Server server;
	
	public void start() throws Exception {
		JettyConfig config = JettyConfig.loadConfig();
		int port = config.getPort();
		logger.info("绑定端口:" + port);
		server = new Server(port);
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		String action = config.getAction();
		
		context.addServlet(new ServletHolder(new LogicServlet()), action);
		
		context.addFilter(JettyEncodingFilter.class, action, EnumSet.of(DispatcherType.REQUEST));
		
		server.setHandler(context);
		server.start();
//		server.join();
	}
	
	public void stop() throws Exception {
		if (server != null) {
			server.stop();
		}
	}
	
}
