package com.example.ramserver.vo;


import lombok.*;

import java.util.Date;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardVo {
    private int boardId;
    private String id;
    private String title;
    private int price;
    private int status;
    private String content;
    private byte[] img;
    private Date boardTime;
    private String nickName;
    private String region;

    public BoardVo(int boardId, String id, String title, int price, int status, String content, byte[] img, Date boardTime)
    {
        this.boardId=boardId;
        this.id=id;
        this.title=title;
        this.price=price;
        this.status=status;
        this.content=content;
        this.img=img;
        this.boardTime=boardTime;
    }

    public BoardVo(int boardId, String id)
    {
        this.boardId=boardId;
        this.id=id;
    }
}
