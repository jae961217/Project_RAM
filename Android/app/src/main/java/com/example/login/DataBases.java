package com.example.login;

import android.provider.BaseColumns;

public class DataBases {
    public static final class CreateDB implements BaseColumns{
        public static final String USERNAME="username";
        public static final String NICKNAME="nickname";
        public static final String PASSWORD="password";
        public static final String PHONENUMBER="phonenumber";
        public static final String EMAIL="email";
        public static final String _TABLENAME0 = "usertable";
        public static final String _CREATE0 = "create table if not exists "+_TABLENAME0+"("
//                +_ID+" integer primary key autoincrement, "
                +USERNAME+" text not null , "
                +EMAIL+" text primary key not null , "
                +PASSWORD+" text not null , "
                +NICKNAME+" text not null , "
                +PHONENUMBER+" text not null, "
                +"UNIQUE("+EMAIL+"))";
        // 이름 닉네임 비밀번호 전화번호 이메일
        // 테이블을 이룰 속성 생성
    }
}
