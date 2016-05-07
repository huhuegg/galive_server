package com.galive.logic.service;

import org.apache.commons.lang.StringUtils;

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

	public PlatformServiceImpl() {
		super();
		appendLog("PlatformServiceImpl");
	}
	
	
	@Override
	public WeChatUser loginWeChat(String udid, String code) throws LogicException {
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
		WeChatUser user = (WeChatUser) platformUserDao.find(unionid, UserPlatform.WeChat);
		if (user == null) {
			user = new WeChatUser();
		}
		//http://wx.qlogo.cn/mmopen/ajNVdqHZLLCqBRT4kbibEibQVaAbuJZcmXNHNYEjZH4b1WtRDIPibafqKEJIYDKyticzvpwkpsLibjNol09OlqdIbmA/0
		String avatar = userInfoResp.getHeadimgurl();
		String avatarThumbnail = avatar.substring(0, avatar.length() - 1) + "132";

		user.setNickname(userInfoResp.getNickname());
		user.setAvatar(avatarThumbnail);
		user.setUdid(udid);
		user.setUnionid(unionid);
		platformUserDao.saveOrUpdate(user);

		appendLog("微信登录成功:" + user.toString());
	
		return user;
	}

	@Override
	public PlatformUser findUser(String udid, UserPlatform platform) throws LogicException {
		PlatformUser u = platformUserDao.find(udid, platform);
		if (u == null) {
			appendLog("用户不存在");
			throw new LogicException("用户不存在");
		}
		return u;
	}

	

	

}
