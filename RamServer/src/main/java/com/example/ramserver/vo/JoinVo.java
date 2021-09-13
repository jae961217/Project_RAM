package com.example.ramserver.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JoinVo {
    private String id;
    private String password;
    private String userName;
    private String phoneNumber;
    private String nickName;
    private String bank;
    private String account;
    private String region;
}
