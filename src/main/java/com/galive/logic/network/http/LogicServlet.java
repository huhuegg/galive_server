package com.galive.logic.network.http;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.galive.logic.network.protocol.CommandIn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.galive.logic.network.http.handler.HttpBaseHandler;

public class LogicServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static Logger logger = LoggerFactory.getLogger(LogicServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		resp.getWriter().write("sharedInstance not support");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		try {
//			CommandIn in = CommandIn.fromHttpReq(req);
//			if (in == null) {
//				logger.error("参数错误");
//				badRequest(resp);
//				return;
//			}
//			// 传递给handler处理业务逻辑
//			HttpBaseHandler command = AnnotationManager.createHttpHandlerInstance(in.getCommand());
//			if (command != null) {
//				String respData = command.handle(in);
//				resp.getWriter().write(respData);
//			} else {
//				badRequest(resp);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error(e.getMessage());
//			badRequest(resp);
//		} finally {
//
//		}
	}
	
	private void badRequest(HttpServletResponse resp) throws IOException {
		logger.error("内部错误");
		resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		resp.getWriter().write("invalidate request");
	}

}
