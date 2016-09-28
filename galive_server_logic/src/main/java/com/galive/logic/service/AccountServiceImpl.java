package com.galive.logic.service;

import java.util.UUID;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang.StringUtils;

import com.galive.logic.dao.AccountDao;
import com.galive.logic.dao.AccountDaoImpl;
import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Account;
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
	public String generateToken(String account) {
		String uuid = UUID.randomUUID().toString();
		String token = Md5Crypt.md5Crypt(uuid.getBytes());
		accountDao.saveToken(account, token);
		return token;
	}

	@Override
	public boolean verifyToken(String account, String token) {
		String existToken = accountDao.findToken(account);
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
	public void saveAccount(Account account) {
		accountDao.save(account);
	}

	@Override
	public Account findAccount(String account) {
		Account act = accountDao.findAccount(account);
		return act;
	}

	@Override
	public WXUserInfoResp reqWechatUserInfo(String code) throws LogicException {
		if (StringUtils.isBlank(code)) {
			appendLog("微信获取用户信息失败，code为空。");
			throw new LogicException("微信获取用户信息失败，code为空。");
		}
		// 获取access_token
		WXAccessTokenResp tokenResp = WeChatRequest.requestAccessToken(code);
		if (tokenResp == null || !StringUtils.isBlank(tokenResp.errcode)) {
			String error = tokenResp == null ? "access_token获取失败。" : tokenResp.getErrmsg();
			String errorCode = tokenResp == null ? "" : tokenResp.getErrcode();
			appendLog("微信获取access_token:" + errorCode + " " + error);
			throw new LogicException("微信登录失败," + error + "(" + errorCode + ")");
		}
		appendLog("获取access_token:" + tokenResp.toString());

		WXUserInfoResp userInfoResp = WeChatRequest.requestUserInfo(tokenResp.getAccess_token(), tokenResp.getOpenid());
		if (userInfoResp == null || !StringUtils.isBlank(userInfoResp.errcode)) {
			String error = userInfoResp == null ? "" : userInfoResp.getErrmsg();
			String errorCode = userInfoResp == null ? "" : userInfoResp.getErrcode();
			appendLog("微信获取用户信息失败:" + errorCode + " " + error);
			throw new LogicException("微信获取用户信息失败，" + error + "(" + errorCode + ")");
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
		
		appendLog("微信登录成功:" + userInfoResp.toString());
		return userInfoResp;
	}

	
}
