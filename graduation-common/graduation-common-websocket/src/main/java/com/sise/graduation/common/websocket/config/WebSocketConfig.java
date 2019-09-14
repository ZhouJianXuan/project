package com.sise.graduation.common.websocket.config;

import com.sise.graduation.common.util.TypeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.util.Map;

/**
 * @ClassName WebSocketConfig
 * @Description
 * @Author CCJ
 * @Date 2019/9/13 14:53
 **/
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {


    @Autowired
    private Map<String, WebSocketHandler> webSocketHandlers;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        if(TypeUtil.isEmpty(webSocketHandlers)) {
            return;
        }

        for (Map.Entry<String, WebSocketHandler> handler : webSocketHandlers.entrySet()) {
            registry.addHandler(handler.getValue(), handler.getKey()).setAllowedOrigins("*");
        }
    }
}
