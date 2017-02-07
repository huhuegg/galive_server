package com.galive.logic.service;


import com.alibaba.fastjson.JSON;
import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Room;
import com.galive.logic.network.socket.handler.push.StartRecordPush;
import com.galive.logic.network.socket.handler.push.StopRecordPush;
import com.galive.logic.network.ws.SessionManager;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RemoteClientServiceImpl extends BaseService implements RemoteClientService {

    private RoomService roomService = new RoomServiceImpl();

    @Override
    public Map<String, Object> register(String clientId) throws LogicException {
        Map<String, Object> result = new HashMap<>();
        result.put("clientId", clientId);

        Room room = roomService.findRoom(RoomService.FindRoomBy.RemoteClient, clientId);
        if (room != null) {
            result.put("publishUrl", room.getRemotePublishUrl());
        } else {
            // 二维码内容
            result.put("qr", clientId);
        }
        return result;
    }

    @Override
    public void bind(String clientId, String publishUrl) throws LogicException {
        Map<String, Object> result = new HashMap<>();
        result.put("ret_code", 0);
        result.put("ret_msg", "");
        result.put("clientId", clientId);
        result.put("command", "BindClient");
        result.put("publishUrl", publishUrl);
        String json = JSON.toJSONString(result);

        Session session = RemoteClientService.sessions.get(clientId);
        if (session != null && session.isOpen()) {
            try {
                session.getRemote().sendString(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Map<String, Object> startRecord(String clientId) throws LogicException {
        Room room = roomService.findRoom(RoomService.FindRoomBy.RemoteClient, clientId);
        if (room != null) {
            StartRecordPush push = new StartRecordPush();
            SessionManager.getInstance().sendMessage(room.getOwnerSid(), push.socketResp());
        }
        Map<String, Object> result = new HashMap<>();
        result.put("ret_code", 0);
        result.put("ret_msg", "");
        return result;
    }

    @Override
    public Map<String, Object> stopRecord(String clientId) throws LogicException {
        Room room = roomService.findRoom(RoomService.FindRoomBy.RemoteClient, clientId);
        if (room != null) {
            StopRecordPush push = new StopRecordPush();
            SessionManager.getInstance().sendMessage(room.getOwnerSid(), push.socketResp());
        }
        Map<String, Object> result = new HashMap<>();
        result.put("ret_code", 0);
        result.put("ret_msg", "");
        return result;
    }

    @Override
    public void unbound(String clientId) throws LogicException {
        Room room = roomService.findRoom(RoomService.FindRoomBy.RemoteClient, clientId);
        if (room != null) {
            room.setRemotePullUrl("");
            room.setRemotePublishUrl("");
            room.setRemoteClientId("");
            roomService.saveOrUpdateRoom(room);
        }
        if (!StringUtils.isEmpty(clientId)) {
            Map<String, Object> result = new HashMap<>();
            result.put("ret_code", 0);
            result.put("ret_msg", "");
            result.put("command", "UnboundClient");
            result.put("qr", clientId);
            String json = JSON.toJSONString(result);
            Session session = RemoteClientService.sessions.get(clientId);
            if (session != null && session.isOpen()) {
                try {
                    session.getRemote().sendString(json);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
// rtmp://222.73.196.99/hls