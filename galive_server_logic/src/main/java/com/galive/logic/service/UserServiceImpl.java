package com.galive.logic.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.galive.logic.dao.UserCacheImpl;
import com.galive.logic.dao.UserDaoImpl;
import com.galive.logic.exception.LogicException;
import com.galive.logic.model.User;

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
		// if (u == null) {
		// return CommandOut.failureOut(Command.USR_INFO, "用户不存在").socketResp();
		// }
		return null;
	}

	@Override
	public List<User> listByLatestLogin(int index, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateUserDeviceToken(String userSid, String deviceToken) throws LogicException {
		// TODO Auto-generated method stub
		// User u = User.findByUsername(userSid);
		// if (u == null) {
		// return CommandOut.failureOut(Command.USR_INFO_MODIFY, "用户不存在");
		// }
	}

}
