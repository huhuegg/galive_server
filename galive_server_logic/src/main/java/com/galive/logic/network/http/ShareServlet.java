package com.galive.logic.network.http;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import com.galive.logic.model.PlatformUser;
import com.galive.logic.model.PlatformUser.UserPlatform;
import com.galive.logic.service.PlatformService;
import com.galive.logic.service.PlatformServiceImpl;

public class ShareServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		resp.getWriter().write("get not support");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String udid = req.getParameter("udid");
			String inviteeDeviceid = req.getParameter("invitee");
			String pt = req.getParameter("platform");
			UserPlatform platform = UserPlatform.convert(NumberUtils.toInt(pt, 0));
			
			PlatformService platformService = new PlatformServiceImpl();
			
			PlatformUser user = platformService.findUserByUdid(udid, platform);
			
			if (user != null) {
				platformService.beContact(user.getDeviceid(), udid, platform);
			} else {
				platformService.saveSharedUdid(inviteeDeviceid, udid);
			}
			resp.getWriter().write("0");
		} catch (Exception e) {
			resp.getWriter().write("1");
		}
	}
	

}
