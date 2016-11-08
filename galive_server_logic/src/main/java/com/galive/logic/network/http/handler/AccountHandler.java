package com.galive.logic.network.http.handler;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.account.Account;
import com.galive.logic.model.account.Gender;
import com.galive.logic.network.http.HttpRequestHandler;
import com.galive.logic.service.AccountService;
import com.galive.logic.service.AccountServiceImpl;

@HttpRequestHandler(desc = "用户信息", command = Command.USR_INFO)
public class AccountHandler extends HttpBaseHandler {

	private AccountService accountService = new AccountServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--AccountHandler(用户信息)--");
		AccountIn in = JSON.parseObject(reqData, AccountIn.class);
		String nickname = in.nickname;
		String avatar = in.avatar;
		String genderStr = in.gender;
		Gender gender = null;
		if (!StringUtils.isEmpty(genderStr)) {
			try {
				gender = Gender.valueOf(genderStr);
			} catch (Exception e) {
				return CommandOut.failureOut(Command.USR_INFO, String.format("参数错误(gender=%s不合法)", genderStr));
			}
		}
		
		String profile = in.profile;
		
		appendLog("昵称(nickname):" + nickname);
		appendLog("头像(avatar):" + avatar);
		appendLog("性别(gender):" + gender);
		appendLog("简介(profile):" + profile);

		Account act = accountService.findAndCheckAccount(account);

		if (nickname != null) {
			act.setNickname(nickname);
		}
		if (avatar != null) {
			act.setAvatar(avatar);
		}
		if (gender != null) {
			act.setGender(gender);
		}
		if (profile != null) {
			act.setProfile(profile);
		}
		accountService.updateAccount(act);
		AccountOut out = new AccountOut();
		out.account = act;
		return out;
	}

	public static class AccountIn {

		public String nickname;
		public String avatar;
		public String gender;
		public String profile;

	}

	public static class AccountOut extends CommandOut {

		public AccountOut() {
			super(Command.USR_INFO);
		}

		public Account account;
	}

}
