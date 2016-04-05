package com.galive.logic.platform.qq;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galive.logic.config.ApplicationConfig;

public class QQRequest {

	private static Logger logger = LoggerFactory.getLogger(QQRequest.class);

	private static final String USER_INFO_URL = "https://graph.qq.com/user/get_user_info";
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * 获取用户个人信息
	 * @param reqUrl
	 * @return
	 */
	public static QQUserInfoResp requestUserInfo(String access_token, String openid) {
		try {
	    	StringBuffer urlBuffer = new StringBuffer(USER_INFO_URL);
	    	urlBuffer.append("?access_token=" + access_token);	
	    	urlBuffer.append("&oauth_consumer_key=" + ApplicationConfig.getInstance().getPlatformConfig().getQq_appid());
	    	urlBuffer.append("&openid=" + openid);
	    	
	    	String result = doGet(urlBuffer.toString());
	    	if (result != null) {
	    		QQUserInfoResp resp = mapper.readValue(result, QQUserInfoResp.class);
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
            String result = "";
            String lines;
            while ((lines = reader.readLine()) != null){  
            	result += lines; 
            }  
            logger.debug(result);
            return result;
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
