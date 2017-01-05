package com.galive.logic.network.ws;

import com.galive.logic.annotation.AnnotationManager;
import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.network.protocol.Command;
import com.galive.logic.network.protocol.CommandIn;
import com.galive.logic.network.socket.handler.WebSocketBaseHandler;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@WebSocket
public class WSLogicHandler extends WebSocketAdapter {

    private static Logger logger = LoggerFactory.getLogger(WSLogicHandler.class);

    private Session session;
    private String account;

    @Override
    public void onWebSocketConnect(Session session) {
        super.onWebSocketConnect(session);
        logger.info("onWebSocketConnect");
        this.session = session;
    }

    @Override
    public void onWebSocketText(String message) {
        super.onWebSocketText(message);
        logger.info("onWebSocketText:" + message);

        CommandIn in = CommandIn.fromSocketReq(message, ApplicationConfig.getInstance().getSocketConfig().getParamsDelimiter());
        if (in != null) {
            account = in.getAccount();
            WebSocketBaseHandler handler = AnnotationManager.createSocketHandlerInstance(in.getCommand());
            if (handler != null) {
                handler.handle(in, session);
            } else {
                logger.error("channelRead 消息错误:" + message);
                //closeAndRemoveChannel(ctx);
            }
        } else {
            logger.error("channelRead 消息错误:" + message);
            //closeAndRemoveChannel(ctx);
        }
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode, reason);
        logger.info("onWebSocketClose");
        if (StringUtils.isEmpty(account)) {
            CommandIn in = new CommandIn();
            in.setAccount(account);
            in.setCommand(Command.OFFLINE);
            WebSocketBaseHandler handler = AnnotationManager.createSocketHandlerInstance(in.getCommand());
            if (handler != null) {
                handler.handle(in, session);
            }
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
    }



}
