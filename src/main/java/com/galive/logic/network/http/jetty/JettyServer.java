package com.galive.logic.network.http.jetty;

import com.galive.logic.network.ws.LogicServlet;
import com.galive.logic.network.ws.RemoveClientServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JettyServer {
	
	private static Logger logger = LoggerFactory.getLogger(JettyServer.class);
	
	private Server server;
	
	public void start() throws Exception {
		JettyConfig config = JettyConfig.loadConfig();
		//int port = config.getPort();
//		int port = 8020;
//		logger.info("绑定端口:" + port);
//		server = new Server(port);
//		ServletHandler command = new ServletHandler();


		//context.set.setContextPath("/");
		//String logicAction = config.getAction();

//		command.addServletWithMapping(EchoServlet.class, "/echo");

		//ontext.addServlet(new ServletHolder(new LogicServlet()), logicAction);

		//context.addFilter(JettyEncodingFilter.class, logicAction, EnumSet.of(DispatcherType.REQUEST));

//		server.setHandler(command);


		Server server = new Server();
		ServerConnector connector = new ServerConnector(server);
		connector.setPort(8020);
		server.addConnector(connector);


		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);

		ServletHolder logicHolder = new ServletHolder("ws-events", LogicServlet.class);
		context.addServlet(logicHolder, "/logic");

		ServletHolder remoteClientHolder = new ServletHolder("ws-events", RemoveClientServlet.class);
		context.addServlet(remoteClientHolder, "/remote_client");

		server.start();
//		server.join();
	}
	
	public void stop() throws Exception {
		if (server != null) {
			server.stop();
		}
	}
	
}
