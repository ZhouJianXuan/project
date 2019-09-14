package com.sise.graduation.common.websocket.obversion;

import org.springframework.web.socket.WebSocketSession;

public interface Listener {
    void open(WebSocketSession session) throws Exception;

    void handleMessage(WebSocketSession session) throws Exception;

    void close(WebSocketSession session) throws Exception;

    void onError(WebSocketSession session) throws Exception;
}
