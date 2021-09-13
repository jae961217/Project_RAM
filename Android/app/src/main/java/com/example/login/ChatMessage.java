package com.example.login;

import com.example.login.MessageType.MessageType;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
public class ChatMessage {
    private String sender;
    private String Message;
    private MessageType msgType;
    public String getMessage(){
        return Message;
    }
    public String getSender(){return sender;}
    public MessageType getMsgType(){return msgType;}
    public ChatMessage(String msg){
        msgType=MessageType.Normal;
        long now=System.currentTimeMillis();
        java.sql.Date date=new java.sql.Date(now);
        SimpleDateFormat sdfNow=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatDate=sdfNow.format(date);
        Message=msg+"("+formatDate+")";
    }

    ///send버튼으로 전송한 메시지일 경우
    public ChatMessage(String sender,String msg){
        msgType=MessageType.Normal;
        this.sender=sender;
        long now=System.currentTimeMillis();
        Date date=new Date(now);
        SimpleDateFormat sdfNow=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatDate=sdfNow.format(date);
        Message=msg+"("+formatDate+")";
    }
    //requst용 메시지 생성자
    public ChatMessage(String sender,String msg,boolean type) throws ParseException {
        msgType=MessageType.Quest;
        this.sender=sender;
        long now=System.currentTimeMillis();
        Date date=new Date(now);
        SimpleDateFormat sdfNow=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatDate=sdfNow.format(date);
        Message=msg+"("+formatDate+")";
    }

    public ChatMessage(String sender,String msg,String Date,boolean chkType) throws ParseException {
        if(chkType==false){
            msgType=MessageType.Normal;

        }
        else{
            msgType=MessageType.Quest;

        }
        this.sender=sender;
        /*SimpleDateFormat sdfNow=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date formatDate=sdfNow.parse(Date);*/
        Message=msg+"("+Date+")";
    }


}
