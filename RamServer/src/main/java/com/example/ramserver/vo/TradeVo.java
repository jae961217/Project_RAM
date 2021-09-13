package com.example.ramserver.vo;

import lombok.Data;
import lombok.Setter;

import java.util.Date;

@Data
@Setter
public class TradeVo {
    private int tradeId;
    private String buyerId;
    private String sellerId;
    private String boardId;
    private Date tradeTime;
    private String title;
}
