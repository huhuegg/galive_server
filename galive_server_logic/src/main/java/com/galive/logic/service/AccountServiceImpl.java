package com.galive.logic.service;

import java.util.UUID;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang.StringUtils;

import com.galive.logic.dao.AccountDao;
import com.galive.logic.dao.AccountDaoImpl;
import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Platform;
import com.galive.logic.model.PlatformAccount;
import com.galive.logic.model.PlatformAccountWeChat;
import com.galive.logic.network.platform.wx.WXAccessTokenResp;
import com.galive.logic.network.platform.wx.WXUserInfoResp;
import com.galive.logic.network.platform.wx.WeChatRequest;

public class AccountServiceImpl extends BaseService implements AccountService {

	private AccountDao accountDao = new AccountDaoImpl();
	
	public AccountServiceImpl() {
		super();
		appendLog("AccountServiceImpl");
	}
	
	@Override
	public String generateToken(String sid) {
		String uuid = UUID.randomUUID().toString();
		String token = Md5Crypt.md5Crypt(uuid.getBytes());
		accountDao.saveToken(sid, token);
		return token;
	}

	@Override
	public boolean verifyToken(String sid, String token) {
		String existToken = accountDao.findToken(sid);
		if (existToken != null) {
			return existToken.equals(token);
		}
		return false;
	}

	@Override
	public boolean verifyAccount(String account) {
		String existToken = accountDao.findToken(account);
		return existToken != null;
	}

	@Override
	public PlatformAccount getPlatformAccountInfo(String sid) throws LogicException {
		PlatformAccount account = accountDao.findPlatformAccount(sid);
		if (account == null) {
			String error = String.format("用户(%s) 不存在", sid);
			appendLog(error);
			throw new LogicException(error); 
		}
		return account;
	}

	@Override
	public PlatformAccountWeChat loginWechat(String code) throws LogicException {
		if (StringUtils.isBlank(code)) {
			appendLog("微信获取用户信息失败，code为空。");
			throw new LogicException("微信获取用户信息失败，code为空。");
		}
		// 获取access_token
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
		// http://wx.qlogo.cn/mmopen/ajNVdqHZLLCqBRT4kbibEibQVaAbuJZcmXNHNYEjZH4b1WtRDIPibafqKEJIYDKyticzvpwkpsLibjNol09OlqdIbmA/0
		String avatar = userInfoResp.getHeadimgurl();
		String nickname = userInfoResp.getNickname();
		String openid = userInfoResp.getOpenid();
		appendLog("微信头像:" + avatar);
		appendLog("微信昵称:" + nickname);
		appendLog("openid:" + openid);
		appendLog("unionid:" + unionid);
		
		PlatformAccountWeChat exist = (PlatformAccountWeChat) accountDao.findPlatformAccount(Platform.WeChat, unionid);
		if (exist != null) {
			PlatformAccountWeChat act = PlatformAccountWeChat.convert(userInfoResp);
			act.setSid(exist.getSid());
			act = (PlatformAccountWeChat) accountDao.savePlatformAccount(act);
			return act;
		}
		appendLog("微信登录成功:" + userInfoResp.toString());
		return exist;
	}

	

	
}
