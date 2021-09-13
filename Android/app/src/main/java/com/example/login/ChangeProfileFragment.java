package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.network.NetworkTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class ChangeProfileFragment extends Fragment {
    private EditText et_change_password;
    private EditText et_check_password;
    private TextView tv_change_password;
    private TextView tv_check_password;
    private ImageView iv_change_password;
    private ImageView iv_check_password;
    private Button btn_change_cancel;
    private Button btn_change_apply;
    private EditText et_change_phoneNumber;
    private TextView tv_change_phoneNumber;
    private ImageView iv_change_phoneNumber;
    private EditText et_change_bank;
    private TextView tv_change_bank;
    private ImageView iv_change_bank;
    private EditText et_change_account;
    private TextView tv_change_account;
    private ImageView iv_change_account;
    private EditText et_change_region;
    private TextView tv_change_region;
    private ImageView iv_change_region;
    Fragment fragment_my_page;

    private boolean correctly_checked=true; // 변경 비밀번호 확인 체크된 경우

    private String id;
    private int point;
    private String nickName;
    private String password;
    private String userName;
    private String phoneNumber;
    private String bank;
    private String account;
    private String region;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_profile, container, false);

        this.InitializeView(view);

        this.GetUserData();

        this.SetButtonClickListener();
        this.SetTextChangeListener();

        return view;
    }

    public void InitializeView(View view){
        et_change_password=(EditText)view.findViewById(R.id.et_change_password);
        et_check_password=(EditText)view.findViewById(R.id.et_check_password);
        tv_change_password=(TextView)view.findViewById(R.id.tv_change_password);
        tv_check_password=(TextView)view.findViewById(R.id.tv_check_password);
        iv_change_password=(ImageView)view.findViewById(R.id.iv_change_password);
        iv_check_password=(ImageView)view.findViewById(R.id.iv_check_password);
        et_change_phoneNumber=(EditText)view.findViewById(R.id.et_change_phoneNumber);
        tv_change_phoneNumber=(TextView)view.findViewById(R.id.tv_change_phoneNumber);
        iv_change_phoneNumber=(ImageView)view.findViewById(R.id.iv_change_phoneNumber);
        et_change_bank=(EditText)view.findViewById(R.id.et_change_bank);
        tv_change_bank=(TextView)view.findViewById(R.id.tv_change_bank);
        iv_change_bank=(ImageView)view.findViewById(R.id.iv_change_bank);
        et_change_account=(EditText)view.findViewById(R.id.et_change_account);
        tv_change_account=(TextView)view.findViewById(R.id.tv_change_account);
        iv_change_account=(ImageView)view.findViewById(R.id.iv_change_account);
        et_change_region=(EditText)view.findViewById(R.id.et_change_region);
        tv_change_region=(TextView)view.findViewById(R.id.tv_change_region);
        iv_change_region=(ImageView)view.findViewById(R.id.iv_change_region);

        btn_change_apply = (Button)view.findViewById(R.id.btn_change_apply);
        btn_change_cancel=(Button)view.findViewById(R.id.btn_change_cancel);

        fragment_my_page = new MyPageFragment();
    }

    // Button 이벤트 리스너 함수
    public void SetButtonClickListener(){
        btn_change_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_change_password.getText().toString().equals("")
                        && et_change_phoneNumber.getText().toString().equals("") && et_change_bank.getText().toString().equals("")
                        && et_change_account.getText().toString().equals("") && et_change_region.getText().toString().equals("")){
                    // 모든 EditText가 빈칸일 경우
                    Toast.makeText(getActivity(), "최소 하나의 정보를 입력하세요", Toast.LENGTH_SHORT).show();
                }
                else if(!correctly_checked){
                    // 변경 비밀번호가 제대로 확인되지 않은경우
                    Toast.makeText(getActivity(), "변경 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                }
                else {
                    PostChangedData();
                    ((MainPageActivity) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment_my_page).commit();
                }
            }
        });

        btn_change_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainPageActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment_my_page).commit();
            }
        });
    }

    // EditText 이벤트 리스너 함수
    public void SetTextChangeListener(){
        // 변경하고자 하는 비밀번호 입력
        et_change_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(et_change_password.getText().toString().length() < 8 || et_change_password.getText().toString().length() > 15)
                {
                    et_check_password.setEnabled(false);

                    tv_change_password.setText("비밀번호는 8 ~ 15자로 작성해주세요");
                    iv_change_password.setVisibility(View.VISIBLE);
                    iv_change_password.setImageResource(R.drawable.error);

                    correctly_checked=false;

                    if(et_change_password.getText().toString().equals("")){
                        correctly_checked=true;
                    }
                }
                else
                {
                    et_check_password.setEnabled(true);

                    tv_change_password.setText("사용 가능한 비밀번호 입니다");
                    iv_change_password.setVisibility(View.VISIBLE);
                    iv_change_password.setImageResource(R.drawable.confirm);

                    correctly_checked=false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 변경하고자 하는 비밀번호와 일치하는지 체크
        et_check_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tv_check_password.setText("비밀번호가 일치하지 않습니다");
                iv_check_password.setVisibility(View.VISIBLE);
                iv_check_password.setImageResource(R.drawable.confirm);

                correctly_checked = false;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(et_check_password.getText().toString().equals(et_change_password.getText().toString()))
                {
                    tv_check_password.setText("비밀번호가 일치합니다");
                    iv_check_password.setVisibility(View.VISIBLE);
                    iv_check_password.setImageResource(R.drawable.confirm);

                    correctly_checked = true;
                }
                else
                {
                    tv_check_password.setText("비밀번호가 일치하지 않습니다");
                    iv_check_password.setVisibility(View.VISIBLE);
                    iv_check_password.setImageResource(R.drawable.error);

                    correctly_checked = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_change_phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_change_bank.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tv_change_bank.setText("영어로 작성해 주세요");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_change_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_change_region.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tv_change_region.setText("영어로 작성해 주세요");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void GetUserData() {
        NetworkTask networkTask = new NetworkTask(getActivity().getApplicationContext(), "http://3.35.48.170:3000/myPage/info", "GET");
        try {
            JSONObject resultObject = new JSONObject(networkTask.execute().get());
            JSONObject userObject = resultObject.optJSONObject("user");

            if (resultObject == null) {
                Log.w("연결결과", "연결실패");
            } else {
                Toast.makeText(getActivity(), resultObject.getString("msg"), Toast.LENGTH_SHORT).show();
                Log.w("resultObject : ", userObject.toString());
                // userObject => {"id":"bmh1211@gmail.com","password":"1234","userName":"Bang","phoneNumber":"010-5014-3278","nickName":"Mino","bank":"HANA","account":"74813243423","point":13,"region":"INCHEON"}

                // 변경할 일 없는 데이터
                id=userObject.getString("id");
                point=userObject.getInt("point");
                nickName=userObject.getString("nickName");
                userName=userObject.getString("userName");

                // 변경 가능한 데이터
                password=userObject.getString("password");
                phoneNumber=userObject.getString("phoneNumber");
                bank=userObject.getString("bank");
                account=userObject.getString("account");
                region=userObject.getString("region");

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void PostChangedData(){
        // 바뀌지 않은 데이터(빈칸으로 남은 데이터)는 기존의 데이터를 넣어주고, 바뀐 데이터에 대해서만 바뀐 데이터를 넣어줌
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("id",id);
            jsonObject.put("userName",userName);
            jsonObject.put("point",point);
            jsonObject.put("nickName",nickName);

            if(!et_change_password.getText().toString().equals("") && correctly_checked){
                jsonObject.put("password",et_change_password.getText().toString());
            }
            else{
                jsonObject.put("password",password);
            }

            if(!et_change_phoneNumber.getText().toString().equals("")){
                jsonObject.put("phoneNumber",et_change_phoneNumber.getText().toString());
            }
            else{
                jsonObject.put("phoneNumber",phoneNumber);
            }

            if(!et_change_bank.getText().toString().equals("")){
                jsonObject.put("bank",et_change_bank.getText().toString());
            }
            else{
                jsonObject.put("bank",bank);
            }

            if(!et_change_account.getText().toString().equals("")){
                jsonObject.put("account",et_change_account.getText().toString());
            }
            else{
                jsonObject.put("account",account);
            }

            if(!et_change_region.getText().toString().equals("")){
                jsonObject.put("region",et_change_region.getText().toString());
            }
            else{
                jsonObject.put("region",region);
            }

        }catch(JSONException e){
            e.printStackTrace();
        }

        NetworkTask networkTask = new NetworkTask(getActivity().getApplicationContext(),"http://3.35.48.170:3000/myPage/update",jsonObject,"POST");
        try{
            JSONObject resultObject = new JSONObject(networkTask.execute().get());

            if(resultObject == null){
                Log.w("연결알림","연결실패");
                return;
            }

            String resultString = resultObject.getString("msg");

            if(resultString.equals("Update Success")){ // 성공했을 경우 오는 response?
                Log.w("resultObject",resultObject.toString());

                Toast.makeText(getActivity(), "정보를 정상적으로 수정했습니다", Toast.LENGTH_SHORT).show();
            }
            else if(resultString.equals("Update Failed")){
                Toast.makeText(getActivity(), "정보 수정에 실패했습니다", Toast.LENGTH_SHORT).show();
            }
        }catch(ExecutionException e){
            e.printStackTrace();
        }catch(InterruptedException e){
            e.printStackTrace();
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public void GetProfileImage(){
        // todo : 프로필 이미지 사진을 받아와야함
    }
}