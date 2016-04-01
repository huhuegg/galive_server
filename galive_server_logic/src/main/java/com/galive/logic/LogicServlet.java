package com.galive.logic;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.CommandIn;
import com.galive.logic.handler.BaseHandler;
import com.galive.logic.helper.AnnotationManager;

public class LogicServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static Logger logger = LoggerFactory.getLogger(LogicServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		resp.getWriter().write("get not support");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.getWriter().write("doPost");
		try {
			String data = req.getParameter("req_data");
			CommandIn in = JSON.parseObject(data, CommandIn.class);
			// 传递给handler处理业务逻辑
			BaseHandler handler = AnnotationManager.createLogicHandlerInstance(in.getCommand());
			String respData = handler.process(in, data);
			resp.getWriter().write(respData);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			try {
				resp.getWriter().write("invalidate request");
			} catch (Exception e2) {
				e.printStackTrace();
			}
		}
	}

}
