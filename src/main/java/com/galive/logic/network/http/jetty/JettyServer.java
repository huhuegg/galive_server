package com.galive.logic.network.http.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
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
		ServletHandler handler = new ServletHandler();
//		context.set.setContextPath("/");
//		String logicAction = config.getAction();
//		
		handler.addServletWithMapping(LogicServlet.class, "/logic");
		
		//ontext.addServlet(new ServletHolder(new LogicServlet()), logicAction);
		
		//context.addFilter(JettyEncodingFilter.class, logicAction, EnumSet.of(DispatcherType.REQUEST));
		
		server.setHandler(handler);
		server.start();
//		server.join();
	}
	
	public void stop() throws Exception {
		if (server != null) {
			server.stop();
		}
	}
	
}
