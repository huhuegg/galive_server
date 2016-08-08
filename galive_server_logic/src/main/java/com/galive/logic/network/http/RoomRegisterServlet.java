package com.galive.logic.network.http;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.galive.logic.service.RoomService;
import com.galive.logic.service.RoomServiceImpl;

public class RoomRegisterServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static Logger logger = LoggerFactory.getLogger(RoomRegisterServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		resp.getWriter().write("get not support");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String ip = req.getRemoteAddr();
			int port = req.getRemotePort();
			String roomsJson = req.getParameter("rooms");
			List<String> rooms = JSON.parseArray(roomsJson, String.class);
			
			RoomService roomService = new RoomServiceImpl();
			roomService.saveRooms(ip, port, rooms);
			resp.getWriter().write("success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			resp.getWriter().write(e.getLocalizedMessage());
		} finally {
			
		}
	}
	
}
