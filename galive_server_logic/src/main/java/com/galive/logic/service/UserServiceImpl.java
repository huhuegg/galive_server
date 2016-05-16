package com.galive.logic.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.dao.UserCache;
import com.galive.logic.dao.UserCacheImpl;
import com.galive.logic.dao.UserDao;
import com.galive.logic.dao.UserDaoImpl;
import com.galive.logic.exception.LogicException;
import com.galive.logic.helper.LogicHelper;
import com.galive.logic.model.User;
import com.galive.logic.model.User.UserGender;
import com.galive.logic.model.User.UserOnlineState;
import com.galive.logic.model.User.UserPlatform;
import com.galive.logic.model.UserExtraData;
import com.galive.logic.model.UserExtraDataApp;
import com.galive.logic.model.UserExtraDataWeChat;
import com.galive.logic.network.socket.ChannelManager;
import com.galive.logic.platform.wechat.WXAccessTokenResp;
import com.galive.logic.platform.wechat.WXUserInfoResp;
import com.galive.logic.platform.wechat.WeChatRequest;

public class UserServiceImpl extends BaseService implements UserService {

	public UserServiceImpl() {
		super();
		appendLog("UserServiceImpl");
	}

	private UserDao userDao = new UserDaoImpl();
	private UserCache userCache = new UserCacheImpl();

	@Override
	public User register(String deviceid, String username, String password, String nickname, String avatar, UserGender gender,
			String profile) throws LogicException {
		User user = userDao.findByUsername(username);
		if (StringUtils.isBlank(deviceid)) {
			String error = "deviceid校验失败。";
			appendLog(error);
			throw new LogicException(error);
		}
		if (user != null) {
			String error = "用户名" + username + "已存在。";
			appendLog(error);
			throw new LogicException(error);
		}
		user = new User();
		if (StringUtils.isBlank(username)) {
			String error = "用户名不能为空。";
			appendLog(error);
			throw new LogicException(error);
		}
		if (StringUtils.isBlank(password)) {
			String error = "密码不能为空。";
			appendLog(error);
			throw new LogicException(error);
		}
		if (StringUtils.isBlank(nickname)) {
			String error = "昵称不能为空。";
			appendLog(error);
			throw new LogicException(error);
		}
		int usernameLen = StringUtils.length(username);
		if (usernameLen < 6 || usernameLen > 20) {
			String error = "用户名必须为6~20位。";
			appendLog(error);
			throw new LogicException(error);
		}
		int passwordLen = StringUtils.length(password);
		if (passwordLen < 6 || passwordLen > 20) {
			String error = "密码必须为6~20位。";
			appendLog(error);
			throw new LogicException(error);
		}
		int nicknameLen = StringUtils.length(nickname);
		if (nicknameLen > 20) {
			String error = "昵称过长，最大为20位。";
			appendLog(error);
			throw new LogicException(error);
		}
		String delimiter = ApplicationConfig.getInstance().getSocketConfig().getParamsDelimiter();
		if (StringUtils.contains(username, delimiter)) {
			String error = "用户名" + username + "包含特殊字符:" + delimiter;
			appendLog(error);
			throw new LogicException(error);
		}
		if (StringUtils.isEmpty(avatar)) {
			String error = "头像为空";
			appendLog(error);
			throw new LogicException(error);
		}
		appendLog("保存用户");
		UserExtraDataApp data = new UserExtraDataApp();
		
		
		data.setUsername(username);
		data.setGender(gender);
		data.setPassword(password);
		data.setNickname(nickname);
		data.setAvatar(avatar);
		data.setProfile(profile);
		user.setExtraData(data);
		user.setDeviceid(deviceid);
		user = userDao.saveOrUpdate(user);
	
		appendLog("更新最后登录时间");
		userCache.updateLatestLogin(user.getSid());
		return user;
	}

	@Override
	public User login(String username, String password) throws LogicException {
		User u = userDao.findByUsername(username);
		if (u == null) {
			String error = "用户不存在";
			appendLog(error);
			throw new LogicException(error);
		} else {
			UserExtraDataApp data = (UserExtraDataApp) u.getExtraData();
			if (!data.getPassword().equals(password)) {
				String error = "密码错误";
				appendLog(error);
				throw new LogicException(error);
			}
		}
		userCache.updateLatestLogin(u.getSid());
		return u;
	}

	@Override
	public User findUserBySid(String userSid) throws LogicException {
		User u = userDao.find(userSid);
		if (u == null) {
			String error = "用户不存在。";
			appendLog(error);
			throw new LogicException(error);
		}
		return u;
	}

	@Override
	public List<User> listByLatestLogin(int index, int size) {
		List<User> users = new ArrayList<>();
		List<String> userSids = userCache.listByLatestLogin(index, index + size - 1);
		for (String sid : userSids) {
			User u = userDao.find(sid);
			if (u != null) {
				users.add(u);
			}
		}
		return users;
	}

	@Override
	public void updateDeviceToken(String userSid, String deviceToken) throws LogicException {
		 User u = userDao.find(userSid);
		 if (u == null) {
		 String error = "用户不存在";
		 appendLog(error);
		 throw new LogicException(error);
		 }
		 if (StringUtils.isBlank(deviceToken)) {
		 String error = "无效的deviceToken";
		 appendLog(error);
		 throw new LogicException(error);
		 }
		userCache.saveDeviceToken(userSid, deviceToken);
	}

	@Override
	public void deleteDeviceToken(String deviceToken) {
		userCache.deleteDeviceToken(deviceToken);
	}

	@Override
	public String findDeviceToken(String userSid) {
		return userCache.findDeviceToken(userSid);
	}

	@Override
	public boolean verifyToken(String userSid, String token) {
		String existToken = userCache.findUserToken(userSid);
		appendLog("exist token:" + existToken);
		appendLog("verify token:" + token);
		if (StringUtils.isBlank(existToken) || !existToken.equals(token)) {
			appendLog("token验证失败。");
			return false;
		}
		return true;
	}

	@Override
	public String createToken(String userSid) throws LogicException {
		String token = LogicHelper.generateRandomMd5();
		appendLog("生成token:" + token + " 绑定用户:" + userSid);
		userCache.saveUserToken(userSid, token);
		return token;
	}

	@Override
	public boolean isOnline(String userSid) {
		boolean online = ChannelManager.getInstance().getOnlineState(userSid) == UserOnlineState.Online;
		return online;
	}

	@Override
	public User loginWeChat(String deviceid, String code, String uid) throws LogicException {
		if (StringUtils.isBlank(deviceid)) {
			throw new LogicException("deviceid验证失败。");
		}
		User u = null;
		if (StringUtils.isBlank(code)) {
			u = userDao.find(uid);
			if (u == null) {
				throw new LogicException("用户不存在。");
			}
			return u;
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
		u = userDao.findWXUserByUnionid(unionid);
		if (u == null) {
			u = new User();
		}
		// http://wx.qlogo.cn/mmopen/ajNVdqHZLLCqBRT4kbibEibQVaAbuJZcmXNHNYEjZH4b1WtRDIPibafqKEJIYDKyticzvpwkpsLibjNol09OlqdIbmA/0
		String avatar = userInfoResp.getHeadimgurl();
//		String avatarThumbnail = avatar.substring(0, avatar.length() - 1) + "132";

		u.setNickname(userInfoResp.getNickname());
		u.setAvatar(avatar);

		UserExtraDataWeChat extra = new UserExtraDataWeChat();
		extra.setPlatform(UserPlatform.WeChat);
		extra.setOpenid(userInfoResp.getOpenid());
		extra.setUnionid(userInfoResp.getUnionid());
		u.setExtraData(extra);
		
		userDao.saveOrUpdate(u);

		appendLog("微信登录成功:" + u.toString());
		return u;
	}

	@Override
	public void beContact(String userSid, String targetSid) {
		userCache.saveContact(userSid, targetSid);
	}

	@Override
	public List<User> listContacts(String userSid, int index, int size) {
		List<User> users = new ArrayList<>();
		List<String> userSids = userCache.listContacts(userSid, index, size < 0 ? -1 : (index + size - 1));
		for (String sid : userSids) {
			User u = userDao.find(sid);
			if (u != null) {
				users.add(u);
			}
		}
		return users;
		
	}

}
