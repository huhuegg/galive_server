package com.galive.logic.network.ws;

import com.alibaba.fastjson.JSON;
import com.galive.logic.exception.LogicException;
import com.galive.logic.network.ws.command.BaseCommand;
import com.galive.logic.network.ws.command.RegisterCommand;
import com.galive.logic.network.ws.command.StartRecordCommand;
import com.galive.logic.network.ws.command.StopRecordCommand;
import com.galive.logic.service.RemoteClientService;
import com.galive.logic.service.RemoteClientServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


@WebSocket
public class WSRemoteClientHandler extends WebSocketAdapter {

    private static Logger logger = LoggerFactory.getLogger(WSRemoteClientHandler.class);



    private Session session;
    private String clientId = "";

    @Override
    public void onWebSocketConnect(Session session) {
        super.onWebSocketConnect(session);
        logger.info("onWebSocketConnect");
        this.session = session;
    }

    @Override
    public void onWebSocketText(String message) {
        super.onWebSocketText(message);
        // 心跳
        if (message.equals("0")) {
            if (session != null && session.isOpen()) {
                try {
                    session.getRemote().sendString("1");
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        logger.info("接收消息:" + message);

        BaseCommand command = JSON.parseObject(message, BaseCommand.class);
        RemoteClientService remoteClientService = new RemoteClientServiceImpl();
        Map<String, Object> result = new HashMap<>();
        try {
            switch (command.getCommand()) {
                case "Register":
                    RegisterCommand registerCommand = JSON.parseObject(message, RegisterCommand.class);
                    clientId = registerCommand.getClientId();
                    RemoteClientService.sessions.put(clientId, session);
                    result = remoteClientService.register(clientId);
                    break;
                case "StartRecord":
                    StartRecordCommand startRecordCommand = JSON.parseObject(message, StartRecordCommand.class);
                    result = remoteClientService.startRecord(startRecordCommand.getClientId());
                    break;
                case "StopRecord":
                    StopRecordCommand stopRecordCommand = JSON.parseObject(message, StopRecordCommand.class);
                    result = remoteClientService.stopRecord(stopRecordCommand.getClientId());
                    break;
            }
            result.put("ret_code", 0);
            result.put("ret_msg", "");
        } catch (LogicException e) {
            result.put("ret_code", 1);
            result.put("ret_msg", e.getMessage());
        }
        result.put("command", command.getCommand());
        String resultJson = JSON.toJSONString(result);
        Session existSession = RemoteClientService.sessions.get(clientId);
        if (existSession != null && existSession.isOpen()) {
            try {
                existSession.getRemote().sendString(resultJson);
                logger.info("发送消息:" + resultJson);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(3000);
//                    remoteClientService.bind(clientId, "");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }).start();

    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode, reason);
        logger.info("onWebSocketClose");
        if (!StringUtils.isEmpty(clientId)) {
            RemoteClientService.sessions.remove(clientId);
        }
    }

    @Override
    public void onWebSocketBinary(byte[] payload, int offset, int len) {
        super.onWebSocketBinary(payload, offset, len);
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        super.onWebSocketError(cause);
        logger.info("onWebSocketError");
        try {
            session.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
