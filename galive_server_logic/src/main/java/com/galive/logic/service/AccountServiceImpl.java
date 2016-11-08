package com.galive.logic.service;

import java.util.UUID;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang.StringUtils;
import com.galive.logic.dao.AccountDao;
import com.galive.logic.dao.AccountDaoImpl;
import com.galive.logic.exception.LogicException;
import com.galive.logic.model.account.Account;
import com.galive.logic.model.account.Gender;
import com.galive.logic.model.account.Platform;
import com.galive.logic.model.account.PlatformAccount;
import com.galive.logic.network.platform.wx.WXAccessTokenResp;
import com.galive.logic.network.platform.wx.WXUserInfoResp;
import com.galive.logic.network.platform.wx.WeChatRequest;

public class AccountServiceImpl extends BaseService implements AccountService {

	private AccountDao accountDao = new AccountDaoImpl();
	private MeetingService meetingService = new MeetingServiceImpl();

	public AccountServiceImpl() {
		super();
		appendLog("AccountServiceImpl");
	}

	@Override
	public Account findAndCheckAccount(String accountSid) throws LogicException {
		Account act = accountDao.findAccount(accountSid);
		if (act == null) {
			throw makeLogicException("账号不存在。");
		}
		return act;
	}

	@Override
	public void updateAccount(Account act) throws LogicException {
		accountDao.saveOrUpdate(act);
	}

	@Override
	public String generateToken(String accountSid) {
		String uuid = UUID.randomUUID().toString();
		String token = Md5Crypt.md5Crypt(uuid.getBytes());
		accountDao.saveToken(accountSid, token);
		return token;
	}

	@Override
	public boolean verifyToken(String accountSid, String token) {
		String existToken = accountDao.findToken(accountSid);
		if (existToken != null) {
			return existToken.equals(token);
		}
		return false;
	}

	@Override
	public boolean verifyAccount(String accountSid) {
		String existToken = accountDao.findToken(accountSid);
		return existToken != null;
	}

	@Override
	public Account login(String accountSid, Platform platform, String platformParams) throws LogicException {
		Account act = null;
		if (!StringUtils.isEmpty(accountSid)) {
			appendLog("登录用户id:" + accountSid);
			act = findAndCheckAccount(accountSid);
			return act;
		} 
		if (platform == null) {
			throw makeLogicException("平台不存在。");
		}
		appendLog("用户注册:" + platform);
		switch (platform) {
		case Guest:
			appendLog("游客注册");
			String uuid = UUID.randomUUID().toString();
			String nickname = "GUEST_" + Md5Crypt.md5Crypt(uuid.getBytes());
			act = new Account();
			act.setNickname(nickname);
			act = accountDao.saveOrUpdate(act);
			meetingService.createMeeting(act);
			break;
		case WeChat:
			if (StringUtils.isEmpty(platformParams)) {
				throw makeLogicException("微信code为空.");
			}
			appendLog("微信注册 code:" + platformParams);
			// 获取access_token
			WXAccessTokenResp tokenResp = WeChatRequest.requestAccessToken(platformParams);
			if (tokenResp == null || !StringUtils.isBlank(tokenResp.errcode)) {
				String error = tokenResp == null ? "access_token获取失败。" : tokenResp.getErrmsg();
				String errorCode = tokenResp == null ? "" : tokenResp.getErrcode();
				throw makeLogicException(String.format("登录失败:%s(%s)", error, errorCode));
			}
			appendLog("获取access_token:" + tokenResp.toString());
			// 获取微信用户信息
			WXUserInfoResp userInfoResp = WeChatRequest.requestUserInfo(tokenResp.getAccess_token(), tokenResp.getOpenid());
			if (userInfoResp == null || !StringUtils.isBlank(userInfoResp.errcode)) {
				String error = userInfoResp == null ? "" : userInfoResp.getErrmsg();
				String errorCode = userInfoResp == null ? "" : userInfoResp.getErrcode();
				throw makeLogicException(String.format("登录失败:%s(%s)", error, errorCode));
			}

			appendLog("获取微信用户信息:" + userInfoResp.toString());
			String unionid = userInfoResp.getUnionid();
			PlatformAccount platformAccount = accountDao.findPlatformAccount(Platform.WeChat, unionid);
			if (platformAccount != null) {
				// 用户已注册
				act = findAndCheckAccount(platformAccount.getAccountSid());
				return act;
			} 
			// http://wx.qlogo.cn/mmopen/ajNVdqHZLLCqBRT4kbibEibQVaAbuJZcmXNHNYEjZH4b1WtRDIPibafqKEJIYDKyticzvpwkpsLibjNol09OlqdIbmA/0
			String avatar = userInfoResp.getHeadimgurl();
			String wxnickname = userInfoResp.getNickname();
			appendLog("微信头像:" + avatar);
			appendLog("微信昵称:" + wxnickname);

			act = new Account();
			act.setAvatar(avatar);
			act.setNickname(wxnickname);
			act.setGender(userInfoResp.getSex() == 1 ? Gender.M : Gender.F);
	
			act = accountDao.saveOrUpdate(act);
			meetingService.createMeeting(act);
			
			platformAccount = new PlatformAccount();
			platformAccount.setAccountSid(act.getSid());
			platformAccount.setPlatform(Platform.WeChat);
			platformAccount.setPlatformUnionid(unionid);
			accountDao.saveOrUpdate(platformAccount);
			
			break;
		}
		
		return act;
	}

	@Override
	public void logout(String accountSid) throws LogicException {
		accountDao.deleteToken(accountSid);
	}
	
}
