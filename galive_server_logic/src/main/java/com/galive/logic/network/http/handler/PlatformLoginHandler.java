package com.galive.logic.network.http.handler;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandIn;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.config.RTCConfig;
import com.galive.logic.config.SocketConfig;
import com.galive.logic.model.PlatformUser.UserPlatform;
import com.galive.logic.model.WeChatUser;
import com.galive.logic.network.http.HttpRequestHandler;
import com.galive.logic.network.model.RespLoginUser;
import com.galive.logic.service.PlatformService;
import com.galive.logic.service.PlatformServiceImpl;
import com.galive.logic.service.UserService;
import com.galive.logic.service.UserServiceImpl;

@HttpRequestHandler(desc = "三方登录", command = Command.USR_LOGIN_PLATFORM)
public class PlatformLoginHandler extends HttpBaseHandler {
	
	private UserService userService = new UserServiceImpl();
	private PlatformService platformService = new PlatformServiceImpl();
	
	@Override
	public String handle(String userSid, String reqData) throws Exception {
		appendLog("--PlatformLoginHandler(三方登录)--");
		PlatformLoginIn in = JSON.parseObject(reqData, PlatformLoginIn.class);

		String udid = in.udid;
		String deviceid = in.deviceid;
		int pt = in.platform;
		
		appendLog("udid:" + udid);
		appendLog("deviceid:" + deviceid);
		appendLog("platform:" + pt);
		
		RespLoginUser respUser = new RespLoginUser();
		UserPlatform platform = UserPlatform.convert(pt);
		String sid = "";
		if (platform == UserPlatform.WeChat) {
			String code = in.wx_code;
			appendLog("微信登录");
			WeChatUser weChatUser;
			if (!StringUtils.isBlank(code)) {
				appendLog("code:" + code);
				weChatUser = platformService.loginWeChat(deviceid, code);
			} else {
				weChatUser = (WeChatUser) platformService.findUserByDeviceid(deviceid, UserPlatform.WeChat);
			}
			respUser.convert(weChatUser);
			sid = weChatUser.getSid();
		}
		
		
		PlatformLoginOut out = new PlatformLoginOut();
		
		out.token =  userService.createToken(sid);
		out.expire = ApplicationConfig.getInstance().getTokenExpire();
		out.user = respUser;
		out.socket_config = ApplicationConfig.getInstance().getSocketConfig();

		String resp = out.httpResp();
		return resp;
	}

	public static class PlatformLoginIn extends CommandIn {
		public String deviceid = "";
		public String udid;
		public int platform;
		public String wx_code = "";
		
	}
	
	public static class PlatformLoginOut extends CommandOut {

		public PlatformLoginOut() {
			super(Command.USR_LOGIN_PLATFORM);
			rtc_config = ApplicationConfig.getInstance().getRtcConfig();
		}

		public RespLoginUser user;
		public String token;
		public String websocketUrl;
		public int expire;
		public RTCConfig rtc_config;
		public SocketConfig socket_config;

	}
	
}
