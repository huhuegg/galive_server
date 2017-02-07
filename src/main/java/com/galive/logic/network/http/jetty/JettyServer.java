package com.galive.logic.network.http.jetty;

import com.galive.logic.network.ws.LogicServlet;
import com.galive.logic.network.ws.RemoveClientServlet;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JettyServer {
	
	private static Logger logger = LoggerFactory.getLogger(JettyServer.class);

	private Server logicServer;
	private Server remoteServer;

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


		logicServer = new Server();
		ServerConnector logicConnector = new ServerConnector(logicServer);
		logicConnector.setPort(8020);
		logicServer.addConnector(logicConnector);


		ServletContextHandler logicContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
		ServletHolder logicHolder = new ServletHolder("logic", LogicServlet.class);
		logicContext.addServlet(logicHolder, "/logic");
		logicServer.setHandler(logicContext);

		remoteServer = new Server();
		ServerConnector remoteConnector = new ServerConnector(remoteServer);
		remoteConnector.setPort(8021);
		remoteServer.addConnector(remoteConnector);

		ServletContextHandler remoteContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
		ServletHolder remoteHolder = new ServletHolder("remote_client", RemoveClientServlet.class);
		remoteContext.addServlet(remoteHolder, "/remote_client");

		remoteServer.setHandler(remoteContext);


		logicServer.start();
		remoteServer.start();
//		server.join();
	}
	
	public void stop() throws Exception {
		if (logicServer != null) {
			logicServer.stop();
		}
		if (remoteServer != null) {
			remoteServer.stop();
		}
	}
	
}
