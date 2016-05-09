package com.galive.logic.service;

import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.galive.logic.dao.PlatformUserCache;
import com.galive.logic.dao.PlatformUserCacheImpl;
import com.galive.logic.dao.PlatformUserDao;
import com.galive.logic.dao.PlatformUserDaoImpl;
import com.galive.logic.exception.LogicException;
import com.galive.logic.model.WeChatUser;
import com.galive.logic.platform.wechat.WXAccessTokenResp;
import com.galive.logic.platform.wechat.WXUserInfoResp;
import com.galive.logic.platform.wechat.WeChatRequest;
import com.galive.logic.model.PlatformUser;
import com.galive.logic.model.PlatformUser.UserPlatform;

public class PlatformServiceImpl extends BaseService implements PlatformService {

	private PlatformUserDao platformUserDao = new PlatformUserDaoImpl();
	private PlatformUserCache platformUserCache = new PlatformUserCacheImpl();
	
	public PlatformServiceImpl() {
		super();
		appendLog("PlatformServiceImpl");
	}
	
	
	@Override
	public WeChatUser loginWeChat(String deviceid, String udid, String code) throws LogicException {
		//获取access_token 
		WXAccessTokenResp tokenResp = WeChatRequest.requestAccessToken(code);
		if (tokenResp == null || !StringUtils.isBlank(tokenResp.errcode)) {
			String error = tokenResp.getErrmsg();
			appendLog("微信获取access_token:" + tokenResp.getErrcode() + " " + error);
			throw new LogicException("微信登录失败," + error);
		}
		appendLog("获取access_token:" + tokenResp.toString());
		
		WXUserInfoResp userInfoResp = WeChatRequest.requestUserInfo(tokenResp.getAccess_token(), tokenResp.getOpenid());
		if (userInfoResp == null || !StringUtils.isBlank(userInfoResp.errcode)) {
			String error = tokenResp.getErrmsg();
			appendLog("微信获取用户信息失败:" + tokenResp.getErrcode() + " " + error);
			throw new LogicException("微信获取用户信息失败:," + error);
		}
		appendLog("获取微信用户信息:" + userInfoResp.toString());
		String unionid = userInfoResp.getUnionid();
		WeChatUser user = (WeChatUser) platformUserDao.find(deviceid, UserPlatform.WeChat);
		if (user == null) {
			user = new WeChatUser();
		}
		//http://wx.qlogo.cn/mmopen/ajNVdqHZLLCqBRT4kbibEibQVaAbuJZcmXNHNYEjZH4b1WtRDIPibafqKEJIYDKyticzvpwkpsLibjNol09OlqdIbmA/0
		String avatar = userInfoResp.getHeadimgurl();
		String avatarThumbnail = avatar.substring(0, avatar.length() - 1) + "132";

		user.setNickname(userInfoResp.getNickname());
		user.setAvatar(avatarThumbnail);
		user.setUdid(udid);
		user.setDeviceid(deviceid);
		user.setUnionid(unionid);
		user.setPlatform(UserPlatform.WeChat);
		platformUserDao.saveOrUpdate(user);

		appendLog("微信登录成功:" + user.toString());
	
		return user;
	}

	@Override
	public PlatformUser findUser(String deviceId, UserPlatform platform) throws LogicException {
		PlatformUser u = platformUserDao.find(deviceId, platform);
		if (u == null) {
			appendLog("用户不存在");
			throw new LogicException("用户不存在");
		}
		return u;
	}
	
	@Override
	public PlatformUser findUserByUdid(String udid, UserPlatform platform) throws LogicException {
		PlatformUser u = platformUserDao.findByUdid(udid, platform);
		return u;
	}


	@Override
	public void saveSharedUdid(String deviceid, String sharedUdid) {
		platformUserCache.saveSharedDeviceid(deviceid, sharedUdid);
	}


	@Override
	public void beContact(String deviceid, String udid, UserPlatform platform) {
		Set<String> deviceids = platformUserCache.listSharedDeviceids(udid);
		for (String contact : deviceids) {
			platformUserCache.saveContact(deviceid, contact);
			platformUserCache.saveContact(contact, deviceid);
			platformUserCache.removeSharedDeviceid(contact, udid);
		}
	}


	

	

	

}
