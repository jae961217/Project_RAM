package com.example.ramserver.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class SellRegisterVo {
    private int boardId;
    private String id;
    private String title;
    private int price;
    private int status;
    private String content;
    private byte[] img;
    private Date boardTime;
}
