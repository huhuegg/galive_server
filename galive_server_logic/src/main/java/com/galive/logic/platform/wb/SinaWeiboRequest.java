package com.galive.logic.platform.wb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SinaWeiboRequest {

	private static Logger logger = LoggerFactory.getLogger(SinaWeiboRequest.class);

	private static final String USER_INFO_URL = "https://api.weibo.com/2/users/show.json";
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * 获取用户个人信息
	 * @param reqUrl
	 * @return
	 */
	public static SinaWeiboUserInfoResp requestUserInfo(String access_token, String userID) {
		try {
	    	StringBuffer urlBuffer = new StringBuffer(USER_INFO_URL);
	    	urlBuffer.append("?access_token=" + access_token);	
	    	urlBuffer.append("&uid=" + userID);
	    	String result = doGet(urlBuffer.toString());
	    	if (result != null) {
	    		SinaWeiboUserInfoResp resp = mapper.readValue(result, SinaWeiboUserInfoResp.class);
	    		return resp;
	    	}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static String doGet(String reqUrl) {
		BufferedReader reader = null;
		HttpURLConnection connection = null;
	
		try {
			URL url = new URL(reqUrl);
            connection = (HttpURLConnection) url.openConnection();  
            connection.connect();  
            
            if (connection.getResponseCode() >= 300) {
                return null;
            }
           
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
            String lines;
            while ((lines = reader.readLine()) != null){  
            	logger.info(lines);  
            	return lines;
            }  
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} finally {
			if (reader != null) {
        		try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        	if (connection != null) {
        		connection.disconnect();
        	}
		}
		return null;
	}
	
	
	
}
