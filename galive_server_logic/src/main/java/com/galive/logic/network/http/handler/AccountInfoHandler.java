package com.galive.logic.network.http.handler;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Meeting;
import com.galive.logic.model.account.Account;
import com.galive.logic.model.account.Gender;
import com.galive.logic.network.http.HttpRequestHandler;
import com.galive.logic.service.AccountService;
import com.galive.logic.service.AccountServiceImpl;
import com.galive.logic.service.MeetingService;
import com.galive.logic.service.MeetingServiceImpl;

@HttpRequestHandler(desc = "用户信息", command = Command.USR_INFO)
public class AccountInfoHandler extends HttpBaseHandler {

	private AccountService accountService = new AccountServiceImpl();
	private MeetingService meetingService = new MeetingServiceImpl();

	@Override
	public CommandOut handle(String account, String reqData) throws Exception {
		appendLog("--AccountHandler(用户信息)--");
		AccountInfoIn in = JSON.parseObject(reqData, AccountInfoIn.class);
		
		String accountSid = in.accountSid;
		appendLog("用户id(accountSid):" + accountSid);
		
		AccountInfoOut out = new AccountInfoOut();
		if (!StringUtils.isEmpty(accountSid)) {
			// 获取用户信息
			Account act = accountService.findAndCheckAccount(accountSid);
			Meeting meeting = meetingService.findMeeting(null, accountSid, null);
			out.account = act;
			out.meeting = meeting;
			return out;
		} else {
			// 修改个人信息
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
			
			out.account = act;
			return out;
		}
		
		
	}

	public static class AccountInfoIn {

		public String accountSid;
		public String nickname;
		public String avatar;
		public String gender;
		public String profile;

	}

	public static class AccountInfoOut extends CommandOut {

		public AccountInfoOut() {
			super(Command.USR_INFO);
		}

		public Account account;
		public Meeting meeting;
	}

}
