package com.example.ramserver.vo;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FindMessageVo {
    private String senderId;
    private String receiverId;
}
