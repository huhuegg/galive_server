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

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.config.FileConfig;



@WebServlet(name = "FileServlet", urlPatterns = "/file")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 30) 
public class FileServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(FileServlet.class);
	
	// 限制访问频率 500次每分钟
	private static Map<String, Integer> reqFrequencyMap = new HashMap<String, Integer>();
	private static final int MAX_ACCESS = 500;
	private static Timer clearTimer = new Timer();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String m = req.getParameter("m");
		if (StringUtils.isEmpty(m)) {
			return;
		}
		String params[] = m.split(paramsSeparator);
		if (params.length != 2) {
			return;
		}
		
		String accountSid = params[0];
		String type = params[1];
		FileConfig config = ApplicationConfig.getInstance().getFileConfig();
		
		String commandFolder = null;
		if (type.equals(t_usr_avatar + "")) {
			commandFolder = config.getFolder_usr_avatar();
		} else if (type.equals(t_meeting_cover + "")) {
			commandFolder = config.getFolder_meeting_cover();
		} else {
			return;
		}
		
		String commandFolderPath = config.getPath() + File.separator + commandFolder;
		String filePath = commandFolderPath + File.separator + accountSid + ".jpg";

		File file = new File(filePath);
		if (file.exists()) {
			FileInputStream fis = new FileInputStream(file);
			byte[] data = IOUtils.toByteArray(fis);
			resp.getOutputStream().write(data);
		}
	}

	private void errorResp(String error, HttpServletResponse resp) {
		Map<String, String> result = new HashMap<>();
		result.put("error", error);
		String json = JSON.toJSONString(result);
		logger.error(error);
		try {
			resp.getWriter().write(json);
		} catch (IOException e) {
			logger.error(e.getLocalizedMessage() + "");
		}
	}
	
	private static final String paramsSeparator = "_";
	private static final int t_usr_avatar = 0;
	private static final int t_meeting_cover = 1;
 	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
		try {
			// 客户端请求，频率拦截
			if (!accessFrequencyFilter(req.getRemoteAddr())) {
				errorResp("请求过于频繁", resp);
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
					data = IOUtils.toByteArray(p.getInputStream());
				}
			}
			
			if (StringUtils.isEmpty(command) || StringUtils.isEmpty(accountSid)) {
				errorResp(String.format("参数错误 command:%s, accountSid:%s", command, accountSid), resp);
				return;
			}
			FileConfig config = ApplicationConfig.getInstance().getFileConfig();
			
			String commandFolder = null;
			int t;
			if (command.equals(Command.FILE_USR_AVATAR)) {
				commandFolder = config.getFolder_usr_avatar();
				t = t_usr_avatar;
			} else if (command.equals(Command.FILE_MEETING_COVER)) {
				commandFolder = config.getFolder_meeting_cover();
				t = t_meeting_cover;
			} else {
				errorResp(String.format("参数错误 command(%s) 不存在", command), resp);
				return;
			}
			String commandFolderPath = config.getPath() + File.separator + commandFolder;
			
			File fileFolder = new File(commandFolderPath);
			if (!fileFolder.exists()) {
				fileFolder.mkdirs();
			}
			
			String filePath = commandFolderPath + File.separator + accountSid + ".jpg";
			File file = new File(filePath);
			
			FileOutputStream fos = new FileOutputStream(file);
			
			IOUtils.write(data, fos);

			if (fos != null) {
				fos.close();
			}
			String m = accountSid + paramsSeparator + t;
			String url = String.format("%s?m=%s", config.getHost(), m);
			Map<String, String> respMap = new HashMap<>();
			respMap.put("url", url);
			String json = JSON.toJSONString(respMap);
			resp.getWriter().write(json); 
		} catch (Exception e) {
			errorResp(e.getLocalizedMessage() + "", resp);
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
