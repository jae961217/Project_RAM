package com.example.login;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.login.MessageType.MessageType;
import com.example.login.network.NetworkTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ChatMessageAdapter extends ArrayAdapter {
    boolean message_left=true;
    Context context;
    List msgs=new ArrayList();
    private String senderId;
    private String otherId;
    public ChatMessageAdapter(Context context,int textViewResourceId,String senderId,String otherId){
        super(context,textViewResourceId);
        this.context=context;
        this.senderId=senderId;
        this.otherId=otherId;
    }

    //@Override
    public void add(ChatMessage object){
        msgs.add(object);
        super.add(object);
    }

    @Override
    public int getCount(){
        return msgs.size();
    }

    @Override
    public ChatMessage getItem(int index){
        return (ChatMessage)msgs.get(index);
    }
    //리스트의 n번째 아이템에 대한 내용을 화면에 출력하는 역할
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row=convertView;
        if(row==null){
            //inflator 생성하여,chatting_message.xml읽어서 VIew객체로 생성
            LayoutInflater inflater=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(R.layout.chatting_message,parent,false);
        }
        //Arrary List 에 들어있는 채팅 문자열을 읽어
        ChatMessage msg=(ChatMessage) msgs.get(position);

        //inflater를 이용해서 생성한 View에 Chatmessage 삽입
        TextView msgText=(TextView)row.findViewById(R.id.chatmessage);
        row.findViewById(R.id.btn_accept).setVisibility(View.GONE);
        row.findViewById(R.id.btn_deny).setVisibility(View.GONE);

        Button acceptBtn=(Button)row.findViewById(R.id.btn_accept);
        acceptBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                NetworkTask fileNetworkTask=new NetworkTask(context,"http://192.168.56.1:3000/trade/applyPurchase?buyer="+otherId+"&owner="+senderId,null,"GET");
                try {
                    Object result=fileNetworkTask.execute().get();
                    if(result==null){
                        System.out.println("데이터 없음");
                    }
                    else{
                    }
                }  catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        msgText.setText(msg.getMessage());
        msgText.setTextColor(Color.parseColor("#000000"));
        if(msg.getSender().equals(senderId)){
            msgText.setBackground(this.getContext().getResources().getDrawable(R.drawable.outbox2));
        }
        else{
            msgText.setBackground(this.getContext().getResources().getDrawable(R.drawable.inbox2));
            if(msg.getMsgType()== MessageType.Quest){
                row.findViewById(R.id.btn_accept).setVisibility(View.VISIBLE);
                row.findViewById(R.id.btn_deny).setVisibility(View.VISIBLE);
            }
        }

        //msgText.setBackground(this.getContext().getResources().getDrawable((message_left?R.drawable.inbox2:R.drawable.outbox2)));

        LinearLayout chatMessageContainer=(LinearLayout)row.findViewById(R.id.chatmessage_container);
        /*int align;
        if(message_left){
            align= Gravity.LEFT;
            message_left=false;
        }
        else{
            align=Gravity.RIGHT;
            message_left=true;
        }*/
        return row;
    }
}
