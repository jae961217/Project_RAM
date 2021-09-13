package com.example.login.listener;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.login.ChattingActivity;
import com.example.login.ChattingProfile;

public class ImageClickListener implements View.OnClickListener {
    String id;
    Context context;
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(context, ChattingProfile.class);
        intent.putExtra("id",id);
        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public ImageClickListener(String id,Context context){
        this.id=id;
        this.context=context;
    }
}
