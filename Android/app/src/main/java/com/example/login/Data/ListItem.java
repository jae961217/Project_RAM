package com.example.login.Data;

public class ListItem {
    String title;
    String userID;
    String tradeTime;
    int boardId;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getTradeTime() {
        return tradeTime;
    }
    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }
    public int getBoardId(){return boardId;}
    public void setBoardId(int boardId){this.boardId = boardId;}
}
