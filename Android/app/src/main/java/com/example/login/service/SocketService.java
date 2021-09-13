package com.example.login.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.login.ChatMessageAdapter;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.SimpleDateFormat;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class SocketService extends Service {

    public static final int MSG_REGISTER_CLIENT = 1;
    //public static final int MSG_UNREGISTER_CLIENT = 2;
    public static final int MSG_SEND_TO_SERVICE = 3;
    public static final int MSG_SEND_TO_ACTIVITY = 4;
    public static final int MSG_SEND_TO_SERVICE_REQ=5;
    private Messenger mClient = null;

    private StompClient mStompClient;
    private String senderId;
    private String receiverId;
    ChatMessageAdapter chatMessageAdapter;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //SErvice 객체와 (Activity사이에서)
        //데이터를 주고받을때 사용하는 메서드
        //데이터를 전달할 필요가 없으면 return null;
        return mMessenger.getBinder();
    }
    private final Messenger mMessenger=new Messenger(new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            Log.w("send test","SocketSerivec - message what : "+msg.what+", msg obj"+msg.obj);
            switch(msg.what){
                case MSG_REGISTER_CLIENT:
                    mClient=msg.replyTo;
                    break;
                case MSG_SEND_TO_SERVICE:
                    try {
                        mStompClient.send("/app/hello", MakeJsonObject(msg.obj.toString(),MSG_SEND_TO_SERVICE).toString()).subscribe();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                case MSG_SEND_TO_SERVICE_REQ:
                    try {
                        mStompClient.send("/app/hello", MakeJsonObject(msg.obj.toString(),MSG_SEND_TO_SERVICE_REQ).toString()).subscribe();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
            return false;
        }
    }));
    private JSONObject MakeJsonObject(String str,int type) throws JSONException {
        JSONObject object=new JSONObject();
        if(type==MSG_SEND_TO_SERVICE_REQ){
            object.put("type","buying");
        }
        else
            object.put("type","normal");
        object.put("author", senderId);
        object.put("receiver",receiverId);
        object.put("msg",str);
        long now=System.currentTimeMillis();
        Date date=new Date(now);
        SimpleDateFormat sdfNow=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatDate=sdfNow.format(date);
        object.put("Date",formatDate);

        return object;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        StompClientConnect();
        StompClientRegister();
        Log.d("test","서비스 onCreate");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        senderId=intent.getStringExtra("sender");
        receiverId=intent.getStringExtra("receiver");
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("test","서비스 onDestroy");
    }
    public void StompClientConnect(){
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws:/192.168.56.1:3000/websockethandler/websocket");
        mStompClient.connect();
    }

    public void StompClientRegister(){
        mStompClient.topic("/topic/roomid").subscribe(topicMessage->{
            Log.d("received message",topicMessage.getPayload());
            JSONObject obj=new JSONObject(topicMessage.getPayload());
            if(!obj.getString("author").equals(senderId)){
                String s= obj.getString("msg");
                //sendMsgToActivity(s);
                sendMsgToActivity(obj);
            }
        });
    }

    public void StompClientDisconnect(){
        mStompClient.disconnect();
    }

    //activity로 메시지 전달
    private void sendMsgToActivity(String str) throws RemoteException {
        Bundle bundle=new Bundle();
        bundle.putString("test",str);
        Message msg=Message.obtain(null,MSG_SEND_TO_ACTIVITY);
        msg.setData(bundle);
        mClient.send(msg);
    }

    //activity로 메시지 전달
    private void sendMsgToActivity(JSONObject object) throws RemoteException {
        Bundle bundle=new Bundle();
        bundle.putString("msgInfo",object.toString());
        Message msg=Message.obtain(null,MSG_SEND_TO_ACTIVITY);
        msg.setData(bundle);
        mClient.send(msg);
    }




}
