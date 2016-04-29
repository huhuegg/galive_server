package com.galive.logic.network.http.handler;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandIn;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.config.RTCConfig;
import com.galive.logic.config.SocketConfig;
import com.galive.logic.model.User;
import com.galive.logic.model.Answer.AnswerResult;
import com.galive.logic.network.http.HttpRequestHandler;
import com.galive.logic.network.model.RespLoginUser;
import com.galive.logic.service.AnswerService;
import com.galive.logic.service.AnswerServiceImpl;
import com.galive.logic.service.QuestionService;
import com.galive.logic.service.QuestionServiceImpl;
import com.galive.logic.service.UserService;
import com.galive.logic.service.UserServiceImpl;

@HttpRequestHandler(desc = "用户登录", command = Command.USR_LOGIN)
public class LoginHandler extends HttpBaseHandler {
	
	private UserService userService = new UserServiceImpl();
	private QuestionService questionService = new QuestionServiceImpl();
	private AnswerService answerService = new AnswerServiceImpl();
	
	@Override
	public String handle(String userSid, String reqData) throws Exception {
		appendLog("--LoginHandler(用户登录)--");
		LoginIn in = JSON.parseObject(reqData, LoginIn.class);

		String username = in.username;
		String password = in.password;
		
		appendLog("用户名:" + username);
		appendLog("密码:" + password);
		
		User u = userService.login(username, password);
		String uid = u.getSid();
		
		LoginOut out = new LoginOut();
		RespLoginUser respUser = new RespLoginUser();
		respUser.convert(u);
		
		out.token =  userService.createToken(uid);
		out.expire = ApplicationConfig.getInstance().getTokenExpire();
		out.user = respUser;
		out.socket_config = ApplicationConfig.getInstance().getSocketConfig();
		out.answerCount = answerService.countAnswer(uid, null);
		out.questionCount  = questionService.countQuestion(uid);
		out.questionSolvedCount = answerService.countAnswer(uid, AnswerResult.Resolved);
		String resp = out.httpResp();
		return resp;
	}

	public static class LoginIn extends CommandIn {
		public String username;
		public String password;
	}
	
	public static class LoginOut extends CommandOut {

		public LoginOut() {
			super(Command.USR_LOGIN);
			rtc_config = ApplicationConfig.getInstance().getRtcConfig();
		}

		public RespLoginUser user;
		public String token;
		public String websocketUrl;
		public int expire;
		public RTCConfig rtc_config;
		public SocketConfig socket_config;
		public long answerCount;
		public long questionCount;
		public long questionSolvedCount;
	}
	
}
