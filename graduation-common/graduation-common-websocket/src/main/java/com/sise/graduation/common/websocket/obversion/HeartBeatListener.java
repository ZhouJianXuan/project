package com.sise.graduation.common.websocket.obversion;

import com.alibaba.fastjson.JSONObject;
import com.sise.graduation.common.util.Maps;
import com.sise.graduation.constant.common.constant.WebsocketConstant;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * @ClassName HeartBeatListener
 * @Description
 * @Author CCJ
 * @Date 2019/9/13 16:59
 **/
@Component(WebsocketConstant.COMMAND_HEART_BEAT)
public class HeartBeatListener implements Listener {

    @Override
    public void open(WebSocketSession session) throws Exception{

    }

    @Override
    public void handleMessage(WebSocketSession session) throws Exception{
        JSONObject json = Maps.of(WebsocketConstant.COMMAND,WebsocketConstant.HEART_BEAT_RESPONSE);
        TextMessage message = new TextMessage(json.toJSONString());
        session.sendMessage(message);
    }

    @Override
    public void close(WebSocketSession session)throws Exception {

    }

    @Override
    public void onError(WebSocketSession session) throws Exception{

    }
}
