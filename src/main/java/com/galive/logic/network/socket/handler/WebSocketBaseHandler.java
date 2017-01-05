package com.galive.logic.network.socket.handler;

import com.galive.logic.exception.LogicException;
import com.galive.logic.network.protocol.Command;
import com.galive.logic.network.protocol.CommandIn;
import com.galive.logic.network.protocol.CommandOut;
import com.galive.logic.network.socket.handler.push.KickOffPush;
import com.galive.logic.network.ws.SessionManager;
import com.galive.logic.service.AccountService;
import com.galive.logic.service.AccountServiceImpl;
import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class WebSocketBaseHandler {
	
	private static Logger logger = LoggerFactory.getLogger(WebSocketBaseHandler.class);
	
	public abstract CommandOut handle(String account, String reqData) throws Exception;

	private StringBuffer logBuffer = new StringBuffer();
	
	public void handle(CommandIn in, Session session) {
		appendSplit();
		long start = System.currentTimeMillis();
		CommandOut out = null;
		String command = in.getCommand();
		String account = in.getAccount();
		String token = in.getToken();
		String tag = in.getTag();
		String params = in.getParams();
		appendLog("请求参数");
		appendLog("command:" + command);
		appendLog("account:" + account);
		appendLog("token:" + token);
		appendLog("tag:" + tag);
		appendLog("params:" + params);
		
		try {
			switch (command) {
				case Command.USR_LOGIN:
					out = handle(account, in.getParams());
					break;
				case Command.ONLINE:
					AccountService accountService = new AccountServiceImpl();
					if (!accountService.verifyToken(account, token)) {
						out = respFail("token已过期", command);
					} else {
						out = handle(account, in.getParams());
					}
					// 是否不同设备连接
					Session old = SessionManager.getInstance().findSession(account);
					if (old != null) {
						KickOffPush push = new KickOffPush();
						pushMessage(account, push.socketResp());
						appendLog("用户重复登录，将帐号踢下线。");
					}
					SessionManager.getInstance().addSession(account, session);
					break;
				default:
					// 是否不同设备连接
					Session existChannel = SessionManager.getInstance().findSession(account);
					if (existChannel != null) {
						out = handle(account, in.getParams());
					}
					break;
			}
			
		} catch (LogicException logicException) {
			logicException.printStackTrace();
			String error = logicException.getMessage();
			appendLog("逻辑错误:" + error);
			out = respFail(error, command);
		} catch (Exception exception) {
			exception.printStackTrace();
			String error = exception.getMessage();
			appendLog("发生错误:" + error);
			out = respFail(error, command);
		}
		if (out != null) {
			out.setTag(tag);
			String resp = out.socketResp();
			appendLog("响应:");
			appendLog(resp);
			appendLog("处理时间:" + (System.currentTimeMillis() - start) + " ms");
			appendSplit();
			logger.info(loggerString());

			if (command.equals(Command.USR_LOGIN)) {
				SessionManager.getInstance().sendMessage(session, resp);
			} else {
				SessionManager.getInstance().sendMessage(account, resp);
			}
		}
	}
	
	void pushMessage(String account, String message) {
		SessionManager.getInstance().sendMessage(account, message);
	}
	
	private CommandOut respFail(String message, String command) {
		return CommandOut.failureOut(command, message);
	}
	
	protected void appendLog(String log) {
		if (logBuffer != null) {
			logBuffer.append("|").append(log);
			logBuffer.append("\n");
		}
	}
	
	private void appendSplit() {
		if (logBuffer != null) {
			logBuffer.append("\n＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝\n");
		}
	}
	
	private String loggerString() {
		return logBuffer == null ? "" : logBuffer.toString();
	}

}
