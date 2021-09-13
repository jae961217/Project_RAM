package com.example.ramserver.vo;

import lombok.Data;

import java.util.Date;

@Data
public class BoardListVo {
    private int boardId;
    private String id;
    private String title;
    private int status;
    private Date boardTime;
    private String nickName;
    private String region;
}
