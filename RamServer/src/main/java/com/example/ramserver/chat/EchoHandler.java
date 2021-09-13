/*
package com.example.ramserver.Chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;

public class EchoHandler extends TextWebSocketHandler {
    private static Logger logger= LoggerFactory.getLogger(EchoHandler.class);
    private Map<String, WebSocketSession> sessions=new HashMap<String,WebSocketSession>();
    //private List<WebSocketSession> sessionList=new ArrayList<WebSocketSession>();


    //채팅방에 입장했을시에
    @Override
    public void afterConnectionEstablished(WebSocketSession session)throws Exception{
        sessions.put(session.getId(),session);
        System.out.println("채팅방 입장 :"+ session.getPrincipal().getName());
    }

    //클라이언트가 웹소켓 서버로 메시지를 전송했을때 실행되는 메소드
    @Override
    protected  void handleTextMessage(WebSocketSession session, TextMessage message)throws Exception{
        logger.info("{}로부터 {} 받음",session.getId(),message.getPayload());

        Iterator<String> sessionIds=sessions.keySet().iterator();
        String sessionId="";
        while(sessionIds.hasNext()){
            sessionId=sessionIds.next();
            sessions.get(sessionId).sendMessage(new TextMessage("echo: "+message.getPayload()));
        }
    }

    ///클라이언트가 연결을 끊었을떄
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status)throws Exception{
        sessions.remove(session.getId());
        System.out.println("채팅방 퇴장자 ; "+session.getPrincipal().getName());
    }
}
*/
