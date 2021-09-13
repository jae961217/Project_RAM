package com.example.ramserver.chat;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){
        config.enableSimpleBroker("/topic");
        //메모리 기반 메시지 브로커가 해당 api를 구독하고 있는 클라이언트에게 메시지 전달
        config.setApplicationDestinationPrefixes("/app");
        //서버에서 클라이언트로부터 메시지를 받을 api의 prefix를 설장
    }
    @Override//클라이언트에서 WebSocket을 연결할 api를 설정
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/websockethandler").withSockJS();
        //파라미터로 넘겨받는 StompEndpointRegistry의 메소드 . 여러가지 endpoint 설정
    }
}
