package com.example.ramserver.model;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MainBoardPost {
    private  int BoardID;
    private String ID;
    private String Title;
    private String Region;
    private Bool Status;
    private String content;
    private String imgPath;
    private int price;

    public MainBoardPost()
    {

    }
}
