package com.galive.logic;

import java.util.Properties;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import com.galive.logic.helper.LogicHelper;

public class JettyServer {
	
	private Server server;
	
	public void start() throws Exception {
		Properties prop = LogicHelper.loadProperties();
		server = new Server(Integer.parseInt(prop.getProperty("http.port", "8080")));
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);
		context.addServlet(new ServletHolder(new LogicServlet()), prop.getProperty("http.action.logic", "/logic"));
		server.start();
//		server.join();
	}
	
	public void stop() throws Exception {
		server.stop();
	}
	
}
