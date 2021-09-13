package com.example.login;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.DataSetObserver;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.login.service.SocketService;

import org.java_websocket.client.WebSocketClient;

import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import okhttp3.WebSocket;
import org.java_websocket.handshake.ServerHandshake;

import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Map;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class ChattingFragment extends Fragment {

    private Messenger mServiceMessenger=null;
    private boolean mIsBound;
    private static final String TAG = "TAG";
    ChatMessageAdapter chatMessageAdapter;
    View view;
    Button messageSendBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_chatting, container, false);
        // Inflate the layout for this fragment
        //버튼 초기화, 클릭 이벤트 추가
        messageSendBtn=(Button)view.findViewById(R.id.btn_send);
        messageSendBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                SendMessage(view);
            }
        });
       /* Intent intent=new Intent(
                getActivity().getApplicationContext(), SocketService.class);
        getActivity().startService(intent);*/
        setStartService();

        //Stomp 연결
        //StompClientConnect();
        //connectWebSocket();
        GetListView();

        return view;
    }
    //서비스 시작
    private void setStartService(){
        getActivity().startService(new Intent(getActivity().getApplicationContext(), SocketService.class));
        getActivity().bindService(new Intent(getActivity().getApplicationContext(), SocketService.class),mConnection, Context.BIND_AUTO_CREATE);
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
                    String value2=msg.getData().getString("test");
                    Log.i("test","act : value2 "+value2);
                    chatMessageAdapter.add(new ChatMessage(value2));
                    break;
            }
            return false;
        }
    }));
    public void GetListView(){
        //chatMessageAdapter=new ChatMessageAdapter(getActivity().getApplicationContext(),R.layout.chatting_message,);
        ListView listView=(ListView)view.findViewById(R.id.listView12);
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

    /*  private void connectWebSocket() {
          URI uri;
          try {
              uri = new URI("ws://192.168.56.1:3000/");
              //uri = new URI("ws://127.0.0.1:3000/websockethandler/websocket");
          } catch (URISyntaxException e) {
              e.printStackTrace();
              return;
          }

          mWebSocketClient = new WebSocketClient(uri) {
              @Override
              public void onOpen(ServerHandshake serverHandshake) {
                  Log.i("Websocket", "Opened");
                  mWebSocketClient.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
              }

              @Override
              public void onMessage(String s) {
                  final String message = s;
                  getActivity().runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          TextView textView = (TextView)getView().findViewById(R.id.message);
                          textView.setText(textView.getText() + "\n" + message);
                      }
                  });
              }

              @Override
              public void onClose(int i, String s, boolean b) {
                  Log.i("Websocket", "Closed " + s);
              }

              @Override
              public void onError(Exception e) {
                  Log.i("Websocket", "Error " + e.getMessage());
              }
          };
          mWebSocketClient.connect();
      }*/
    //버튼 클릭 이벤트
    public void SendMessage(View view){
        EditText etMsg=(EditText)getView().findViewById(R.id.etMessage);
        String strMsg=(String)etMsg.getText().toString();

        chatMessageAdapter.add(new ChatMessage(strMsg));
        sendMessageToservice(strMsg);


        //mStompClient.send("/app/hello", strMsg);
    }
    //서비스로 메시지 전송
    private void sendMessageToservice(String str){
        if(mIsBound){
            if(mServiceMessenger!=null){
                try{
                    Message msg=Message.obtain(null, SocketService.MSG_SEND_TO_SERVICE,str);
                    msg.replyTo=mMessenger;
                    mServiceMessenger.send(msg);
                } catch (RemoteException e) {

                }
            }
        }
    }
}