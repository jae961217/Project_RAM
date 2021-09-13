package com.example.ramserver.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;


@Data
@AllArgsConstructor
public class ChatInsertVo {
    private int messageId;
    private int chatRoomId;
    private String author;
    private String receiver;
    private String msg;
    private java.util.Date Date;
    private String type;
}
