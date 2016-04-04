package com.galive.logic.service;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.galive.logic.dao.UserCacheImpl;
import com.galive.logic.dao.UserDaoImpl;
import com.galive.logic.exception.LogicException;
import com.galive.logic.helper.LogicHelper;
import com.galive.logic.model.User;
import com.galive.logic.model.User.UserOnlineState;
import com.galive.logic.network.socket.ChannelManager;

public class UserServiceImpl implements UserService {

	private UserDaoImpl userDao = new UserDaoImpl();
	private UserCacheImpl userCache = new UserCacheImpl();

	@Override
	public User register(String username, String password, String nickname) throws LogicException {
		User u = userDao.findUserByUsername(username);
		if (u != null) {
			throw new LogicException("用户名已存在。");
		}
		u = new User();
		if (StringUtils.isBlank(username)) {
			throw new LogicException("用户名不能为空。");
		}
		if (StringUtils.isBlank(password)) {
			throw new LogicException("密码不能为空。");
		}
		if (StringUtils.isBlank(nickname)) {
			throw new LogicException("昵称不能为空。");
		}
		int usernameLen = StringUtils.length(username);
		if (usernameLen < 6 || usernameLen > 20) {
			throw new LogicException("用户名必须为6~20位。");
		}
		int passwordLen = StringUtils.length(password);
		if (passwordLen < 6 || passwordLen > 20) {
			throw new LogicException("密码必须为6~20位。");
		}
		int nicknameLen = StringUtils.length(nickname);
		if (nicknameLen > 20) {
			throw new LogicException("昵称最大为20位。");
		}
		u.setUsername(username);
		u.setPassword(password);
		u.setNickname(nickname);
		u = userDao.saveUser(u);
		userCache.updateLatestLogin(u.getSid());
		return u;
	}

	@Override
	public User login(String username, String password) throws LogicException {
		User u = userDao.findUserByUsername(username);
		if (u == null) {
			throw new LogicException("用户不存在");
		} else {
			if (!u.getPassword().equals(password)) {
				throw new LogicException("密码错误");
			}
		}
		userCache.updateLatestLogin(u.getSid());
		return u;
	}

	@Override
	public User findUserBySid(String userSid) throws LogicException {
		User u = userDao.findUser(userSid);
		if (u == null) {
			throw new LogicException("用户不存在");
		}
		return u;
	}

	@Override
	public List<User> listByLatestLogin(int index, int size) {
		List<User> users = userCache.listByLatestLogin(index, index + size - 1);
		return users;
	}

	@Override
	public void updateUserDeviceToken(String userSid, String deviceToken) throws LogicException {
		User u = userDao.findUser(userSid);
		if (u == null) {
			throw new LogicException("用户不存在。");
		}
		if (StringUtils.isBlank(deviceToken)) {
			throw new LogicException("无效的deviceToken。");
		}
		userCache.saveDeviceToken(userSid, deviceToken);
	}
	
	@Override
	public void deleteUserDeviceToken(String userSid) {
		userCache.deleteDeviceToken(userSid);
	}
	
	@Override
	public String findDeviceToken(String userSid) {
		return userCache.findDeviceToken(userSid);
	}

	@Override
	public boolean verifyToken(String userSid, String token) {
		String existToken = userCache.findUserToken(userSid);
		if (StringUtils.isBlank(existToken) || !existToken.equals(token)) {
			return false;
		}
		return true;
	}

	@Override
	public String createToken(String userSid) throws LogicException {
		String token = LogicHelper.generateRandomMd5();
		userCache.saveUserToken(userSid, token);
		return token;
	}

	@Override
	public boolean isOnline(String userSid) {
		boolean online = ChannelManager.getInstance().getOnlineState(userSid) == UserOnlineState.Online;
		return online;
	}

	

	

}
