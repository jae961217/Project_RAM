package com.example.ramserver.model;

import lombok.Data;

@Data
public class Message {
    private String Name;
    public Message(String name){
        this.Name=name;
    }
}
