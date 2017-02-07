package com.galive.logic.network.ws;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;


@WebServlet(name = "RemoveClientServlet", urlPatterns = { "/remote_client" })
public class RemoveClientServlet extends WebSocketServlet {

	private static final long serialVersionUID = 1L;
	
	private static Logger logger = LoggerFactory.getLogger(RemoveClientServlet.class);


	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.getPolicy().setIdleTimeout(30000);
		factory.register(WSRemoteClientHandler.class);
	}
}
