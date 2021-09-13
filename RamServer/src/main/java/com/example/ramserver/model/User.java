package com.example.ramserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class User {
    private String id;
    private String password;
    private String userName;
    private String phoneNumber;
    private String nickName;
    private String bank;
    private String account;
    private int point;
    private String region;
}
