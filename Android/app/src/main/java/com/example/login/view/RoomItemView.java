package com.example.login.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.login.R;
import com.example.login.listener.ImageClickListener;

public class RoomItemView extends LinearLayout {

    TextView textView, textView2;
    ImageView imageView;
    Context context;
    public RoomItemView(Context context) {
        super(context);
        init(context);
    }
    public RoomItemView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);

    }

    private void init(Context context){
        this.context=context;
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.chatting_room,this,true);

        textView=findViewById(R.id.textView);
        textView2=findViewById(R.id.textView2);
        imageView=findViewById(R.id.imageView);
    }

    public void setName(String name){
        textView.setText(name);
    }
    public void setMobile(String mobile){
        textView2.setText(mobile);
    }
    public void setBitmap(Bitmap bitmap){
        String s=textView.getText().toString();
        imageView.setOnClickListener(new ImageClickListener(textView.getText().toString(),context));
        imageView.post(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap,imageView.getWidth(),imageView.getHeight(),false));

            }
        });
    }

}
