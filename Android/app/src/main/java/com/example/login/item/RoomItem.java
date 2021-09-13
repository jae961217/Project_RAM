package com.example.login.item;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class RoomItem {
    String name;
    String mobile;
    int resId;
    Bitmap bitmap;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public Bitmap getBitmap(){return bitmap;}
    public void setBitmap(Bitmap bitmap) {this.bitmap=bitmap;}

    public RoomItem(String name,String mobile,int resId){
        this.name=name;
        this.mobile=mobile;
        this.resId=resId;
    }
    public RoomItem(String name,String mobile,byte[] bytes){
        this.name=name;
        this.mobile=mobile;
        this.bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        System.out.println(name);
    }

    @Override
    public String toString(){
        return "RoomItem{"+
                "name='"+name+'\''+
                ", mobile='"+mobile+'\''+
                '}';
    }


}
