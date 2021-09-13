package com.example.login;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.login.Data.PostingF;

import java.util.ArrayList;

public class Adapter extends BaseAdapter {
    public ArrayList<PostingF> listviewitem = new ArrayList<PostingF>();
    private final ArrayList<PostingF> arrayList = listviewitem;

    @Override
    public int getCount()
    {
        return arrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getIndex(int position)
    {
        return arrayList.get(position).getIndex();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {    //list에 내용 적용
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.posting_item_theme, parent, false);

            TextView name = (TextView)convertView.findViewById(R.id.posting_nickName);
            TextView title = (TextView)convertView.findViewById(R.id.posting_title);
            TextView date = (TextView)convertView.findViewById(R.id.posting_date);
            TextView region = (TextView)convertView.findViewById(R.id.posting_region);
            TextView status = (TextView)convertView.findViewById(R.id.posting_status);
            TextView index = (TextView)convertView.findViewById(R.id.posting_index);



            holder.name = name;
            holder.region = region;
            holder.status = status;
            holder.title = title;
            holder.date = date;
            holder.index = index;



            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }

        PostingF posting = arrayList.get(position);
        holder.name.setText(posting.getName());
        holder.title.setText(posting.getTitle());
        holder.date.setText(posting.getDate());
        holder.index.setText(posting.getIndex());
        holder.region.setText(posting.getRegion());
        holder.status.setText(posting.getStatus());


        return convertView;
    }

    public void addItem(String date, String name, String title, String index, String region, String status) {
        PostingF posting = new PostingF();

        posting.setName(name);
        posting.setTitle(title);
        posting.setDate(date);
        posting.setIndex(index);
        posting.setRegion(region);
        posting.setStatus(status);

        listviewitem.add(posting);
    }

    //List 삭제 method
    public void removeItem(int position) {
        if(listviewitem.size() < 1) {

        }
        else {
            listviewitem.remove(position);
        }
    }

    public void removeItem() {
        if(listviewitem.size() < 1) {

        }
        else {
            listviewitem.remove(listviewitem.size()-1);
        }
    }

    static class ViewHolder {
        TextView name, date, title, index, region, status;
    }
}
