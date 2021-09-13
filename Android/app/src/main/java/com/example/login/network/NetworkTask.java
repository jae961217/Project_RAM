package com.example.login.network;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.login.HTTPConnetction;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.internal.http.HttpMethod;

public class NetworkTask extends AsyncTask<Void, Void, String> {

    String url;
    JSONObject values;
    String method;
    Context context;
    String filepath;

    public NetworkTask(Context context,String url, JSONObject values, String HttpMethod){
        this.url = url;
        this.values = values;
        this.method=HttpMethod;
        this.context=context;
    }
    public NetworkTask(Context context,String url,String HttpMethod){
        this.url = url;
        this.values=new JSONObject();
        this.method=HttpMethod;
        this.context=context;
    }
    public NetworkTask(Context context,String url, JSONObject values,String filePath, String HttpMethod){
        this.url = url;
        this.values = values;
        this.method=HttpMethod;
        this.context=context;
        this.filepath=filePath;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //progress bar를 보여주는 등등의 행위
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected String doInBackground(Void... params) {
        JSONObject result=null;
        HTTPConnetction requestHttpURLConnection = new HTTPConnetction();
        if(method.equals("POST")){
            result = requestHttpURLConnection.HttpPost(context,url, values);
        }
        else if(method.equals("GET")){
            result = requestHttpURLConnection.HttpGet(context,url);
        }
        else if(method.equals("FILE")){
            try {
                result=requestHttpURLConnection.HttpFilePost(context,url,filepath,values);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("GETTest")){
            result = requestHttpURLConnection.HttpTest(context,url);
        }
        else{
            result=null;
        }
        return result.toString(); // 결과가 여기에 담깁니다. 아래 onPostExecute()의 파라미터로 전달됩니다.
    }

    @Override
    protected void onPostExecute(String result) {
        // 통신이 완료되면 호출됩니다.
        // 결과에 따른 수정 등은 여기서 합니다.
        // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
    }
}

