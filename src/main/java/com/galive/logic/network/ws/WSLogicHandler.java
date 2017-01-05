package com.galive.logic.network.ws;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@WebSocket
public class WSLogicHandler extends WebSocketAdapter {

    private static Logger logger = LoggerFactory.getLogger(WSLogicHandler.class);

    @Override
    public void onWebSocketConnect(Session sess) {
        super.onWebSocketConnect(sess);
        logger.info("onWebSocketConnect");
    }

    @Override
    public void onWebSocketText(String message) {
        super.onWebSocketText(message);
        logger.info("onWebSocketText:" + message);
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode, reason);
        logger.info("onWebSocketClose");
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
