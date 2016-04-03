package com.galive.logic.service;

import java.util.List;

import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Room;
import com.galive.logic.model.User;

public class UserServiceImpl implements UserService {

	@Override
	public User register(String username, String password, String nickname) throws LogicException {
//		if (u != null) {
//			return respFail("用户已注册");
//		}
//		u = new User();
//		u.setUsername(in.username);
//		u.setPassword(in.password);
//		u.setNickname(in.nickname);
//		u.save();
//		u.updateLatestLogin();
		return null;
	}
	
	@Override
	public User login(String username, String password) throws LogicException {
		// TODO Auto-generated method stub
		
//		User u = User.findByUsername(in.username);
//		if (u == null) {
//			return respFail("用户不存在");
//		} else {
//			if (!u.getPassword().equals(in.password)) {
//				return respFail("密码错误");
//			}
//		}
//		u.updateLatestLogin();
		return null;
	}
	
	@Override
	public User findUserBySid(String userSid) throws LogicException {
//		if (u == null) {
//			return CommandOut.failureOut(Command.USR_INFO, "用户不存在").socketResp();
//		}
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
//		User u = User.findByUsername(userSid);
//		if (u == null) {
//			return CommandOut.failureOut(Command.USR_INFO_MODIFY, "用户不存在");
//		}
	}

	


	

	

	

	

}
