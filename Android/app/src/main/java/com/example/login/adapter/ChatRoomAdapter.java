package com.example.login.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.login.item.RoomItem;
import com.example.login.view.RoomItemView;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomAdapter extends BaseAdapter {
    ArrayList<RoomItem> items=new ArrayList<RoomItem>();
    Context context;

    public ChatRoomAdapter(Context context){
        this.context=context;
    }
    @Override
    public int getCount() {
        return items.size();
    }

    public void addItem(RoomItem item){
        items.add(item);
    }
    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RoomItemView roomItemView=null;

        if(convertView==null){
            roomItemView=new RoomItemView(context);
        }
        else{
            roomItemView=(RoomItemView)convertView;
        }

        RoomItem item=items.get(position);
        roomItemView.setName(item.getName());
        roomItemView.setMobile(item.getMobile());
        roomItemView.setBitmap(item.getBitmap());


        return roomItemView;
    }

}









