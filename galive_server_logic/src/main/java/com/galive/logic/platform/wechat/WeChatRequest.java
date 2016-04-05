package com.galive.logic.platform.wechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.config.PlatformConfig;

public class WeChatRequest {

	private static Logger logger = LoggerFactory.getLogger(WeChatRequest.class);

	private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
	private static final String USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo";
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	
	/**
	 * 获取AccessToken
	 * @param code
	 * @return
	 */
	public static WXAccessTokenResp requestAccessToken(String code) {
		try {
			PlatformConfig cfg = ApplicationConfig.getInstance().getPlatformConfig();
	    	StringBuffer urlBuffer = new StringBuffer(ACCESS_TOKEN_URL);
	    	urlBuffer.append("?appid=" + cfg.getWechat_appid());	
	    	urlBuffer.append("&secret=" + cfg.getWechat_appsecret());	
	    	urlBuffer.append("&code=" + code);	
	    	urlBuffer.append("&grant_type=" + "authorization_code");	
	    	
	    	String result = doGet(urlBuffer.toString());
	    	if (result != null) {
	    		WXAccessTokenResp resp = mapper.readValue(result, WXAccessTokenResp.class);
	    		return resp;
	    	}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
    	return null;
	}
	
	/**
	 * 获取用户个人信息
	 * @param reqUrl
	 * @return
	 */
	public static WXUserInfoResp requestUserInfo(String access_token, String openid) {
		try {
	    	StringBuffer urlBuffer = new StringBuffer(USER_INFO_URL);
	    	urlBuffer.append("?access_token=" + access_token);	
	    	urlBuffer.append("&openid=" + openid);
	    	String result = doGet(urlBuffer.toString());
	    	if (result != null) {
	    		WXUserInfoResp resp = mapper.readValue(result, WXUserInfoResp.class);
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
