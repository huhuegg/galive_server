package com.galive.logic.network.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galive.common.protocol.Command;
import com.galive.logic.service.BaseService;



@WebServlet(name = "FileServlet", urlPatterns = "/file")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 30) 
public class FileServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(FileServlet.class);
	
	// 限制访问频率 500次每分钟
	private static Map<String, Integer> reqFrequencyMap = new HashMap<String, Integer>();
	private static final int MAX_ACCESS = 500;
	private static Timer clearTimer = new Timer();
	
	private String uploadPath(HttpServletRequest req) {
		//String uploadPath = req.getServletPath();
		return "/usr/galive";
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String account = req.getParameter("a");
		String type = req.getParameter("t");
		String uploadPath = uploadPath(req);
		String folder = null;
		if (type.equals("0")) {
			folder = "avatar";
		} else if (type.equals("1")) {
			folder = "meeting_cover";
		} else {
			resp.getWriter().write("-1");
			return;
		}
		
		folder = uploadPath + File.separator + folder;
		
		String path = folder + File.separator + account;
		File file = new File(path);
		if (file.exists()) {
			FileInputStream fis = new FileInputStream(file);
			byte[] data = IOUtils.toByteArray(fis);
			resp.getOutputStream().write(data);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
		try {
			// 客户端请求，频率拦截
			if (!accessFrequencyFilter(req.getRemoteAddr())) {
				resp.getWriter().write("-1");
				return;
			}
			String command = null;
			String accountSid = null;
			byte[] data = null;
			Collection<Part> parts = req.getParts();
			for (Part p : parts) {
				String name = p.getName();
				if (name.equals("command")) {
					command = IOUtils.toString(p.getInputStream(), StandardCharsets.UTF_8);
				} else if (name.equals("accountSid")) {
					accountSid = IOUtils.toString(p.getInputStream(), StandardCharsets.UTF_8);
				} else {
					//String type = p.getContentType();
					//long size = p.getSize();
					//String fileName = p.getSubmittedFileName();
					//SBMessageFile file = new SBMessageFile();
					data = IOUtils.toByteArray(p.getInputStream());
				}
			}
			
			if (StringUtils.isEmpty(command) || StringUtils.isEmpty(accountSid)) {
				resp.getWriter().write("-1");
				return;
			}
			String uploadPath = uploadPath(req);
			String folder = null;
			String t = "";
			if (command.equals(Command.FILE_USR_AVATAR)) {
				folder = "avatar";
				t = "0";
			} else if (command.equals(Command.FILE_MEETING_COVER)) {
				folder = "meeting_cover";
				t = "1";
			} else {
				resp.getWriter().write("-1");
				return;
			}
			folder = uploadPath + File.separator + folder;
			
			File fileFolder = new File(folder);
			if (!fileFolder.exists()) {
				fileFolder.mkdirs();
			}
			
			String path = folder + File.separator + accountSid;
			File file = new File(path);
			
			FileOutputStream fos = new FileOutputStream(file);
			
			IOUtils.write(data, fos);

			if (fos != null) {
				fos.close();
			}
			String result = String.format("http://127.0.0.1/galive/file?a=%s&t=", accountSid, t);
			resp.getWriter().write(result); 
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage() + "");
			try {
				resp.getWriter().write("-1");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	private boolean accessFrequencyFilter(String ip) {
		if (StringUtils.isEmpty(ip))
			return true;
		Integer count = reqFrequencyMap.get(ip);
		if (count == null) {
			reqFrequencyMap.put(ip, 1);
		} else {
			if (count >= MAX_ACCESS) {
				return false;
			} else {
				reqFrequencyMap.put(ip, ++count);
			}
		}
		return true;
	}
	
	static {
		clearTimer.schedule(new ClearTask(), 0, 1000 * 60);
	}
	
	static class ClearTask extends TimerTask {

		public void run() {
			reqFrequencyMap.clear();
		}
	}

}
