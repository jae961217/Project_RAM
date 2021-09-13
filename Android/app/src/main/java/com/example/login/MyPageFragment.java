package com.example.login;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.network.NetworkTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MyPageFragment extends Fragment {
    private Toolbar tb_myPage;
    private ImageView iv_profile;
    private TextView tv_name_real;
    private TextView tv_nickname_real;
    private TextView tv_email_real;
    private TextView tv_favoriteRegion_real;
    private TextView tv_phoneNumber_real;
    private TextView tv_bank_real;
    private TextView tv_bankaccount_real;
    private TextView tv_point_real;

    // 팝업 chooser 기본틀
    Fragment fragment1;
    Fragment fragment_change_profile;
    Button btn_changeProfile;
    Button btn_list_buy_sell_trade;

    // 팝업 chooser에 들어가는 요소들
    private PopupWindow pw_chooser;
    private Button btn_buy_list;
    private Button btn_sell_list;
    private Button btn_trade_list;
    Fragment fragment_buy_list;
    Fragment fragment_sell_list;
    Fragment fragment_trade_list;
    private String password_check;

    // 정보 수정 버튼 클릭 시 비밀번호 체크에 들어가는 요소
    private PopupWindow pw_checkPW;
    private Button btn_check;
    private EditText et_present_password;

    private SharedPreferences.Editor sharedPreferences_qr_editor;

    // 테스트용
    private Button btn_sell;
    private Button btn_buy;
    private Fragment fragment_buy;
    private Fragment fragment_sell_qr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);

//        tb_myPage = (Toolbar)view.findViewById(R.id.tb_myPage);
        fragment1 = new BoardFragment();
        fragment_change_profile = new ChangeProfileFragment();
        btn_changeProfile=(Button)view.findViewById(R.id.btn_changeProfile);
        btn_list_buy_sell_trade=(Button)view.findViewById(R.id.btn_list_buy_sell_trade);

        iv_profile = (ImageView) view.findViewById(R.id.iv_profile);
        tv_name_real = (TextView) view.findViewById(R.id.tv_name_real);
        tv_nickname_real = (TextView) view.findViewById(R.id.tv_nickname_real);
        tv_email_real = (TextView) view.findViewById(R.id.tv_email_real);
        tv_favoriteRegion_real = (TextView) view.findViewById(R.id.tv_favoriteRegion_real);
        tv_phoneNumber_real = (TextView) view.findViewById(R.id.tv_phoneNumber_real);
        tv_bank_real = (TextView) view.findViewById(R.id.tv_bank_real);
        tv_bankaccount_real = (TextView) view.findViewById(R.id.tv_bankaccount_real);
        tv_point_real = (TextView) view.findViewById(R.id.tv_point_real);

        sharedPreferences_qr_editor = getActivity().getSharedPreferences("setting", Context.MODE_PRIVATE).edit();

        // 테스트용
        btn_sell = (Button)view.findViewById(R.id.btn_sell);
        fragment_sell_qr = new SellQrFragment();
        btn_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent qr_intent = new Intent(getContext(),SellQrActivity.class);
//                startActivity(qr_intent);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment_sell_qr).commit();
            }
        });
        btn_buy = (Button)view.findViewById(R.id.btn_buy);
        fragment_buy = new BuyFragment();
        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment_buy).commit();
            }
        });

        // 마이페이지에 유저 데이터 출력
        this.GetUserData();

        // 마이페이지에 유저 프로필 사진 출력
        // todo : this.함수();

        btn_list_buy_sell_trade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPopupChooser();
            }
        });

//        setSupportActionBar(tb_myPage);
//        getSupportActionBar().setTitle("My Page");
//        getSupportActionBar().setDisplayShowCustomEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //자동으로 뒤로가기 버튼을 만들어줌
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dialog_close_dark); //뒤로가기버튼 모양

        btn_changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(container.getContext(), "정보수정", Toast.LENGTH_SHORT).show();
                //((MainPageActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment_change_profile).commit();
                pwCheckPopUp();
            }
        });

        return view;
    }

    public void GetUserData(){
        NetworkTask networkTask = new NetworkTask(getActivity().getApplicationContext(),"http://3.35.48.170:3000/myPage/info","GET");
        //NetworkTask networkTask=new NetworkTask(getActivity(),"http://192.168.56.1:3000/myPage/info","GET");
        try{
            JSONObject resultObject = new JSONObject(networkTask.execute().get());
            JSONObject userObject = resultObject.optJSONObject("user");

            if(resultObject == null){
                Log.w("연결결과","연결실패");
            }
            else{
                Toast.makeText(getActivity(), resultObject.getString("msg"), Toast.LENGTH_SHORT).show();
                Log.w("resultObject : ",userObject.toString());

                // userObject => {"id":"bmh1211@gmail.com","password":"1234","userName":"Bang","phoneNumber":"010-5014-3278","nickName":"Mino","bank":"HANA","account":"74813243423","point":13,"region":"INCHEON"}
                tv_name_real.setText(userObject.getString("userName"));
                tv_nickname_real.setText(userObject.getString("nickName"));
                tv_email_real.setText(userObject.getString("id"));
                tv_favoriteRegion_real.setText(userObject.getString("region"));
                tv_phoneNumber_real.setText(userObject.getString("phoneNumber"));
                tv_bank_real.setText(userObject.getString("bank"));
                tv_bankaccount_real.setText(userObject.getString("account"));
                tv_point_real.setText(Integer.toString(userObject.getInt("point")));

                // 정보수정 창으로 들어갈 때 체크를 위한 비밀번호 받아놓기
                password_check = userObject.getString("password");
            }
        }catch(InterruptedException e){
            e.printStackTrace();
        }catch(ExecutionException e){
            e.printStackTrace();
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public void setPopupChooser(){
        View popupView = getLayoutInflater().inflate(R.layout.popupwindow_chooser_buy_sell_trade,null);
        pw_chooser = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        pw_chooser.setFocusable(true);
        pw_chooser.showAtLocation(popupView, Gravity.CENTER,0,0);

        btn_buy_list = (Button)pw_chooser.getContentView().findViewById(R.id.btn_buy_list);
        btn_sell_list = (Button)pw_chooser.getContentView().findViewById(R.id.btn_sell_list);
        btn_trade_list = (Button)pw_chooser.getContentView().findViewById(R.id.btn_trade_list);

        btn_buy_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_buy_list = new BuyListFragment();
                ((MainPageActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment_buy_list).commit();

                pw_chooser.dismiss();
            }
        });

        btn_sell_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_sell_list = new SellListFragment();
                ((MainPageActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment_sell_list).commit();

                pw_chooser.dismiss();
            }
        });

        btn_trade_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 마이페이지로 거래목록 접근 시 sharedPreferences_qr을 false로 잡아줘서 아이템 클릭시 게시판에서 일치하는 게시물로 이동하도록
                sharedPreferences_qr_editor.putBoolean("flag_qr",false);
                sharedPreferences_qr_editor.commit();

                fragment_trade_list = new TradeListFragment();
                ((MainPageActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment_trade_list).commit();

                pw_chooser.dismiss();
            }
        });
    }

    public void pwCheckPopUp(){
        View popupView = getLayoutInflater().inflate(R.layout.popupwindow_check_password,null);
        pw_checkPW = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        pw_checkPW.setFocusable(true);
        pw_checkPW.showAtLocation(popupView, Gravity.CENTER,0,0);

        et_present_password = (EditText)pw_checkPW.getContentView().findViewById(R.id.et_present_password);
        btn_check = (Button)pw_checkPW.getContentView().findViewById(R.id.btn_check);

        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("입력한 비밀번호", et_present_password.getText().toString());
                Log.w("가져온 비밀번호", password_check);

                if (et_present_password.getText().toString().equals(password_check)) {
                    // 비밀번호가 일치하면 정보수정 프레그먼트로 이동
                    ((MainPageActivity) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment_change_profile).commit();

                    pw_checkPW.dismiss();
                } else {
                    // 비밀번호가 일치하지 않으면 써놓은 비밀번호 지우기
                    Toast.makeText(getActivity(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                    et_present_password.setText(null);
                }
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        //return super.onCreateOptionsMenu(menu);
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.menu_change_profile,menu);
//
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        //다른 메뉴를 만들경우 여기에 id별 기능 추가(switch - case 사용)
//        switch(item.getItemId()){
//            case android.R.id.home:
//                finish();
//                return true;
//
//            case R.id.item_change_profile:
//                Intent intent_changeProfile = new Intent(this, ChangeProfileActivity.class);
//
//                Toast.makeText(getApplicationContext(), "정보수정 버튼 누름", Toast.LENGTH_SHORT).show();
//                startActivity(intent_changeProfile);
//                return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
// 툴바 관련 reference : https://www.hanumoka.net/2017/10/28/android-20171028-android-toolbar/
// 리스트뷰 관련 reference : https://recipes4dev.tistory.com/42
// 팝업윈도우 관련 reference : https://puzzleleaf.tistory.com/48