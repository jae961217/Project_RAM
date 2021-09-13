package com.example.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.login.item.RoomItem;
import com.example.login.network.NetworkTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;
import java.util.concurrent.ExecutionException;


public class ChattingProfile extends AppCompatActivity {

    private String UserName;
    ImageView profileImage;
    TextView nameText;
    Button chattingBtn;
    Button userInfoBtn;
    Intent thisIntent;
    Bitmap imageBitmap;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chatting_profile);
        thisIntent=getIntent();
        UserName=thisIntent.getStringExtra("id");
        profileImage=findViewById(R.id.profile_img);
        nameText=findViewById(R.id.profile_name);
        chattingBtn=findViewById(R.id.btn_chat);
        userInfoBtn=findViewById(R.id.btn_UserInfo);


        NetworkTask networkTask=new NetworkTask(getApplicationContext(),"http://192.168.56.1:3000/chat/profileImage?id="+UserName,null,"GET");
        JSONObject resultObject=null;
        try {
            Object result=networkTask.execute().get();
            if(result==null){
                System.out.println("데이터 없음");
            }
            else{
                resultObject=new JSONObject((String)result);
                JSONArray nameArray=resultObject.getJSONArray("name");
                JSONArray dataArray=resultObject.getJSONArray("data");

                for(int i=0;i<nameArray.length();i++){
                    nameText.setText(nameArray.get(i).toString());
                }
                for(int i=0;i<dataArray.length();i++){
                    System.out.println("바이트 배열"+dataArray.get(i).toString());
                    byte[] decodedBytes= Base64.getDecoder().decode(dataArray.get(i).toString());
                    imageBitmap= BitmapFactory.decodeByteArray(decodedBytes,0,decodedBytes.length);
                    profileImage.post(new Runnable() {
                        @Override
                        public void run() {
                            profileImage.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap,profileImage.getWidth(),profileImage.getHeight(),false));
                        }
                    });
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}