package com.example.ramserver.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@AllArgsConstructor
public class EnterChatResponse {
    private int roomId;
    private String enterId;
    private String otherId;
    private String message;
    private String date;
    private String type;



}
