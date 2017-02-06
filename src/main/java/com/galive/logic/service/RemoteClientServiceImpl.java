package com.galive.logic.service;


import com.alibaba.fastjson.JSON;
import com.galive.logic.exception.LogicException;

import java.util.HashMap;
import java.util.Map;

public class RemoteClientServiceImpl extends BaseService implements RemoteClientService {


    @Override
    public Map<String, Object> register(String clientId) throws LogicException {
        Map<String, Object> result = new HashMap<>();
        // 二维码内容
        Map<String, String> qrCode = new HashMap<>();
        qrCode.put("clientId", "clientId");
        String qrJson = JSON.toJSONString(qrCode);
        result.put("qr", qrJson);

        // TODO 是否需要重新绑定

        return result;
    }

    @Override
    public void bind(String clientId, String publishUrl) throws LogicException {
        // TODO bangding
        Map<String, Object> result = new HashMap<>();
        result.put("ret_code", 0);
        result.put("ret_msg", "31231");
        result.put("command", "BindClient");
        result.put("publishUrl", "publishUrlxxxxxxxx");
    }

    @Override
    public Map<String, Object> startRecord(String clientId) throws LogicException {
        // TODO 通知手机端开始录屏 拉流地址
        return null;
    }

    @Override
    public Map<String, Object> stopRecord(String clientId) throws LogicException {
        // TODO 通知手机端结束录屏
        return null;
    }

    @Override
    public void unbound(String clientId) throws LogicException {
        // TODO 解绑
    }


}
// rtmp://222.73.196.99/hls