package com.example.ramserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatMessage {
    private String type;
    private String author;
    private String receiver;
    private String msg;
    private String Date;
}
