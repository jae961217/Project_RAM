package com.example.login;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login.network.NetworkTask;
import com.example.login.service.SocketService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.concurrent.ExecutionException;

public class ChattingActivity extends AppCompatActivity {
    Button requestBuyingBtn;
    Button messageSendBtn;
    ChatMessageAdapter chatMessageAdapter;
    private static final String TAG = "TAG";
    private boolean mIsBound;
    private Messenger mServiceMessenger=null;
    private Intent thisIntent;
    private String SenderName;
    private String OtherName;
    private TextView otherNameTextView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisIntent=getIntent();
        OtherName=thisIntent.getStringExtra("id");
        setContentView(R.layout.fragment_chatting);
        otherNameTextView=(TextView)findViewById(R.id.otherName);
        otherNameTextView.setText(OtherName);

        requestBuyingBtn=(Button)findViewById(R.id.btn_request);
        messageSendBtn=(Button)findViewById(R.id.btn_send);
        messageSendBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                SendMessage();
            }
        });
        requestBuyingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SendBuyingRequest();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        NetworkTask fileNetworkTask=new NetworkTask(getApplicationContext(),"http://192.168.56.1:3000/chat/enterChattingRoom?id="+OtherName,null,"GET");
        JSONObject resultObject=null;
        try {
            Object result=fileNetworkTask.execute().get();
            if(result==null){
                System.out.println("데이터 없음");
            }
            else{
                resultObject=new JSONObject((String)result);
                System.out.println(resultObject);
                SenderName=resultObject.get("enterId").toString();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setStartService();
        GetListView();
        try {
            setChatting(resultObject);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void GetListView(){
        chatMessageAdapter=new ChatMessageAdapter(getApplicationContext(),R.layout.chatting_message,SenderName,OtherName);
        ListView listView=(ListView)findViewById(R.id.listView12);
        listView.setAdapter(chatMessageAdapter);
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        //메시지가 추가되었을때 마지막 메시지를 스크롤할수 있는 리스트뷰를 만든다
        chatMessageAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged(){
                super.onChanged();
                listView.setSelection(chatMessageAdapter.getCount()-1);
            }
        });
    }
    private void setChatting(JSONObject jsonObject) throws JSONException, ParseException {
       JSONArray chatArray= jsonObject.getJSONArray("messageSet");
       for(int i=0;i<chatArray.length();i++){
           JSONObject tmpObj=chatArray.getJSONObject(i);
           ChatMessage msg=null;
           if(tmpObj.getString("type").equals("normal")){
               msg=new ChatMessage(tmpObj.getString("enterId"),tmpObj.getString("message"),tmpObj.getString("date"),false);
           }
           else{
               msg=new ChatMessage(tmpObj.getString("enterId"),tmpObj.getString("message"),tmpObj.getString("date"),true);
           }
           chatMessageAdapter.add(msg);
       }
    }
    //서비스 시작
    private void setStartService(){
        Intent intent=new Intent(getApplicationContext(),SocketService.class);
        intent.putExtra("sender",SenderName);
        intent.putExtra("receiver",OtherName);
        startService(intent);
       bindService(intent,mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    private ServiceConnection mConnection=new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("test","onServiceconnected");
            mServiceMessenger=new Messenger(iBinder);
            try{
                Message msg=Message.obtain(null,SocketService.MSG_REGISTER_CLIENT);
                msg.replyTo=mMessenger;
                mServiceMessenger.send(msg);
            } catch (RemoteException e) {
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }

    };

    //service로부터 message 받음
    private final Messenger mMessenger=new Messenger(new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            Log.i("test receive","act :what "+msg.what);
            switch(msg.what){
                case SocketService.MSG_SEND_TO_ACTIVITY:
                    //String value2=msg.getData().getString("test");
                    String value2=msg.getData().getString("msgInfo");
                    JSONObject resObj=null;
                    try {
                        resObj=new JSONObject(value2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.i("test","act : value2 "+value2);
                    try {
                        if(resObj.getString("type").equals("normal")){
                        chatMessageAdapter.add(new ChatMessage(resObj.getString("author"),resObj.getString("msg")));
                        }
                        else{
                            chatMessageAdapter.add(new ChatMessage(resObj.getString("author"),resObj.getString("msg"),true));
                        }
                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }
                    break;
            }
                    return false;
        }
    }));
    //send 버튼 클릭 이벤트
    public void SendMessage(){
        EditText etMsg=(EditText)findViewById(R.id.etMessage);
        String strMsg=(String)etMsg.getText().toString();

        //chatMessageAdapter.add(new ChatMessage(strMsg));
        chatMessageAdapter.add(new ChatMessage(SenderName,strMsg));
        sendMessageToservice(strMsg,"normal");
    }
    /// 구매신청 버튼 클릭시 서버와의 통신
    private void SendBuyingRequest() throws ParseException {
        String strMsg=SenderName+" apply purchase";
        chatMessageAdapter.add(new ChatMessage(SenderName,strMsg,true));
        sendMessageToservice(strMsg,"req");

    }
    //서비스로 메시지 전송
    private void sendMessageToservice(String str,String type){
        if(mIsBound){
            if(mServiceMessenger!=null){
                try{
                    Message msg=null;
                    if(type.equals("normal")){
                        msg=Message.obtain(null, SocketService.MSG_SEND_TO_SERVICE,str);
                    }
                    else{
                        msg= msg=Message.obtain(null, SocketService.MSG_SEND_TO_SERVICE_REQ,str);
                    }
                    msg.replyTo=mMessenger;
                    mServiceMessenger.send(msg);
                } catch (RemoteException e) {

                }
            }
        }
    }




}
