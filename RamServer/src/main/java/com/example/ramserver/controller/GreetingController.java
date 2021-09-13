package com.example.ramserver.controller;

import com.example.ramserver.model.ClientMessage;
import com.example.ramserver.model.Greeting;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.util.HtmlUtils;

@Configuration
public class GreetingController {
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(ClientMessage message)throws Exception{
        Thread.sleep(1000);
        System.out.println(message.getName());
        return new Greeting("Hello "+ HtmlUtils.htmlEscape(message.getName()));
    }
}
