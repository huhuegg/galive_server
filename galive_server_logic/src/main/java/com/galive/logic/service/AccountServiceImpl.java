package com.galive.logic.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang.StringUtils;

import com.galive.logic.dao.AccountDao;
import com.galive.logic.dao.AccountDaoImpl;
import com.galive.logic.exception.LogicException;
import com.galive.logic.model.MeetingMemberOptions;
import com.galive.logic.model.MeetingOptions;
import com.galive.logic.model.account.Account;
import com.galive.logic.model.account.Platform;
import com.galive.logic.model.account.PlatformAccount;
import com.galive.logic.model.account.PlatformAccountGuest;
import com.galive.logic.model.account.PlatformAccountWeChat;
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
	public PlatformAccount login(String accountSid, Platform platform, Map<String, Object> params) throws LogicException {
		if (platform == null) {
			throw makeLogicException("平台不存在。");
		}
		String platformSid = (String) params.get("platformSid");
		PlatformAccount platformAccount = null;
		switch (platform) {
		case Guest:
			String name = (String) params.get("name");
			if (StringUtils.isEmpty(name)) {
				throw makeLogicException("用户名为空。");
			}
			if (StringUtils.isEmpty(platformSid)) {
				PlatformAccountGuest guest = new PlatformAccountGuest();
				guest.setName(name);
				guest.setPlatform(Platform.Guest);
				platformAccount = createOrBindAccount(accountSid, guest);
			} else {
				platformAccount = accountDao.findPlatformAccount(platformSid);
			}
			break;
		case WeChat:
			String code = (String) params.get("wechatCode");
			if (StringUtils.isEmpty(code)) {
				// 验证微信用户
				
				if (StringUtils.isEmpty(platformSid)) {
					throw makeLogicException("platformSid为空");
				}
				platformAccount = accountDao.findPlatformAccount(platformSid);
			} else {
				// 微信登录
				// 获取access_token
				WXAccessTokenResp tokenResp = WeChatRequest.requestAccessToken(code);
				if (tokenResp == null || !StringUtils.isBlank(tokenResp.errcode)) {
					throw makeLogicException(String.format("登录失败:%s(%s)", tokenResp.getErrmsg(), tokenResp.getErrcode()));
				}
				appendLog("获取access_token:" + tokenResp.toString());
				//
				WXUserInfoResp userInfoResp = WeChatRequest.requestUserInfo(tokenResp.getAccess_token(), tokenResp.getOpenid());
				if (userInfoResp == null || !StringUtils.isBlank(userInfoResp.errcode)) {
					throw makeLogicException(String.format("登录失败:%s(%s)", userInfoResp.getErrmsg(), userInfoResp.getErrcode()));
				}
				appendLog("获取微信用户信息:" + userInfoResp.toString());
				String unionid = userInfoResp.getUnionid();
				// //
				// http://wx.qlogo.cn/mmopen/ajNVdqHZLLCqBRT4kbibEibQVaAbuJZcmXNHNYEjZH4b1WtRDIPibafqKEJIYDKyticzvpwkpsLibjNol09OlqdIbmA/0
				String avatar = userInfoResp.getHeadimgurl();
				String nickname = userInfoResp.getNickname();
				String openid = userInfoResp.getOpenid();
				appendLog("微信头像:" + avatar);
				appendLog("微信昵称:" + nickname);
				appendLog("openid:" + openid);
				appendLog("unionid:" + unionid);

				PlatformAccountWeChat exist = (PlatformAccountWeChat) accountDao.findPlatformAccount(platform, unionid);
				if (exist != null) {
					// 已存在
					platformAccount = exist;
				} else {
					// 首次微信登录
					PlatformAccountWeChat platformAccountWeChat = PlatformAccountWeChat.convert(userInfoResp);
					platformAccount = createOrBindAccount(accountSid, platformAccountWeChat);
				}
			}
		}
		platformAccount = accountDao.saveOrUpdatePlatformAccount(platformAccount);
		Account act = accountDao.findAccount(platformAccount.getAccountSid());
		act.setLatestLoginPlatform(platformAccount.getSid());
		accountDao.saveOrUpdateAccount(act);
		appendLog(platformAccount.toString());
		return platformAccount;
	}

	private PlatformAccount createOrBindAccount(String accountSid, PlatformAccount platformAccount) throws LogicException {
		if (StringUtils.isEmpty(accountSid)) { // 首次登录 生成新账号
			Account act = Account.createNewAccount();
			accountSid = accountDao.saveOrUpdateAccount(act).getSid();
		} else { // 绑定现有账号
			Account act = findAndCheckAccount(accountSid);
			// 检查该账号是否绑定过该平台账号。
			// 如绑过，则生成新Account，否则绑定Account
			List<PlatformAccount> platformAccounts = accountDao.listPlatformAccounts(accountSid);
			boolean bind = false;
			for (PlatformAccount pa : platformAccounts) {
				if (pa.getPlatform() == platformAccount.getPlatform()) {
					bind = true;
					break;
				}
			}
			if (bind) {
				return createOrBindAccount(null, platformAccount);
			} else {
				accountSid = act.getSid();
			}
		}
		platformAccount.setAccountSid(accountSid);
		return platformAccount;
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
	public void logout(String accountSid) throws LogicException {
		accountDao.deleteToken(accountSid);
	}

	@Override
	public MeetingOptions updateMeetingOptions(String accountSid, MeetingOptions options) throws LogicException {
		Account act = accountDao.findAccount(accountSid);
		act.setMeetingOptions(options);
		return options;
	}

	@Override
	public MeetingMemberOptions updateMeetingMemberOptions(String accountSid, MeetingMemberOptions options) throws LogicException {
		Account act = accountDao.findAccount(accountSid);
		act.setMeetingMemberOptions(options);
		return options;
	}

	@Override
	public PlatformAccount findPlatformAccount(String platformAccountSid) throws LogicException {
		PlatformAccount act = accountDao.findPlatformAccount(platformAccountSid);
		return act;
	}

}
