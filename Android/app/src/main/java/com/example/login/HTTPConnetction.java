package com.example.login;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import okhttp3.MultipartBody;

public class HTTPConnetction { //ddddd
    public static JSONObject  HttpTest(Context context,String Url) {
        JSONObject result = null;
        try {
            URL url = new URL(Url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestProperty("key", "value");
            connection.setRequestMethod("GET");
            //connection.setRequestProperty("Accept","multipart/form-data");
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setConnectTimeout(15000); //통신 타임아웃
            setCookieHeader(connection,context);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                result=new JSONObject(response.toString());
            } else {
                return null;
            }
        } catch (ConnectException e) {
            Log.e("test", "ConnectionException");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }



    public static JSONObject  HttpGet(Context context,String Url) {
        JSONObject result = null;
        try {
            URL url = new URL(Url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestProperty("key", "value");
            connection.setRequestMethod("GET");
            //.setRequestProperty("Accept","multipart/form-data");
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setConnectTimeout(15000); //통신 타임아웃
            setCookieHeader(connection,context);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                result=new JSONObject(response.toString());
            } else {
                return null;
            }
        } catch (ConnectException e) {
            Log.e("test", "ConnectionException");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static JSONObject HttpPost(Context context,String URL, JSONObject jsonObject)  {
        JSONObject result = null;
        try{
            URL url = new URL(URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestProperty("content-type", "application/json; utf-8");
            connection.setRequestProperty("Accept","application/json");
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            //connection.setConnectTimeout(15000);
            setCookieHeader(connection,context);
            String jsonInputString  = jsonObject.toString();  //보낼데이터
            try(OutputStream os = connection.getOutputStream()){
                byte[] input = jsonInputString.getBytes("utf-8"); //전송할값
                os.write(input,0,input.length);
                os.flush();
                os.close();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                getCookieHeader(connection,context);
                result = new JSONObject(response.toString());
            } else {
                return null;
            }

        } catch (ConnectException e) {
            Log.e("test", "ConnectException");
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static JSONObject HttpFilePost(Context context, String URL, String filePath, JSONObject jsonObject) throws IOException {
        JSONObject result = null;
        File file=new File(filePath);

        try{
            URL url = new URL(URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            String boundary= UUID.randomUUID().toString();
            connection.setRequestProperty("content-type", "multipart/form-data;boundary="+boundary);
            connection.setRequestProperty("Accept","application/json");
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            //connection.setConnectTimeout(15000);
            setCookieHeader(connection,context);

            DataOutputStream request=new DataOutputStream(connection.getOutputStream());
            request.writeBytes("--" + boundary + "\r\n");
            request.writeBytes("Content-Disposition: form-data; name=\"description\"\r\n\r\n");
            request.writeBytes("File content" + "\r\n");

            if(jsonObject!=null){
                request.writeBytes("--" + boundary + "\r\n");
                request.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + "json"+ "\"\r\n\r\n");
                request.write(jsonObject.toString().getBytes());
                request.writeBytes("\r\n");

            }
            request.writeBytes("--" + boundary + "\r\n");
            request.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"\r\n\r\n");
            //byte[] imageBytes= Files.readAllBytes(file.toPath());
            request.write(FileUtils.readFileToByteArray(file));
            request.writeBytes("\r\n");

            request.writeBytes("--" + boundary + "--\r\n");
            request.flush();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                getCookieHeader(connection,context);
                //result= new MultipartBody();
                result = new JSONObject(response.toString());
            } else {
                return null;
            }

        } catch (ConnectException e) {
            Log.e("test", "ConnectException");
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }


    //쿠키 설정
    private static void setCookieHeader(HttpURLConnection con,Context context){
        SharedPreferences pref = context.getSharedPreferences("sessionCookie", Context.MODE_PRIVATE);
        String sessionid = pref.getString("sessionid",null);
        if(sessionid!=null) {
            Log.d("LOG","세션 아이디"+sessionid+"가 요청 헤더에 포함 되었습니다.");
            con.setRequestProperty("Cookie", sessionid);
        }
    }
    private static void getCookieHeader(HttpURLConnection con,Context context){
        List<String> cookies=con.getHeaderFields().get("Set-Cookie");
        if(cookies!=null){
            for(String cookie: cookies){
                String sessionID=cookie.split(";\\s*")[0];
                //JSESSOINID=~~~
                //세션 아이디가 포함된 쿠키를 얻음
                setSessionIdInSharedPref(sessionID,context);
            }
        }
    }

    private static void setSessionIdInSharedPref(String sessionID,Context context) {
        SharedPreferences pref = context.getSharedPreferences("sessionCookie", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        if(pref.getString("sessionid",null) == null){ //처음 로그인하여 세션아이디를 받은 경우
            Log.d("LOG","처음 로그인하여 세션 아이디를 pref에 넣었습니다."+sessionID);
        }else if(!pref.getString("sessionid",null).equals(sessionID)){ //서버의 세션 아이디 만료 후 갱신된 아이디가 수신된경우
            Log.d("LOG","기존의 세션 아이디"+pref.getString("sessionid",null)+"가 만료 되어서 "
                    +"서버의 세션 아이디 "+sessionID+" 로 교체 되었습니다.");
        }
        edit.putString("sessionid",sessionID);
        edit.apply(); //비동기 처리
    }



}
