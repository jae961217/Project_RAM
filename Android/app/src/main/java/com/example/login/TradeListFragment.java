package com.example.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.Data.ListItem;
import com.example.login.adapter.TradeListAdapter;
import com.example.login.network.NetworkTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class TradeListFragment extends Fragment {
    private String str_CurrentPhotoPath = "";
    final static int REQUEST_TAKE_PHOTO = 99;
    private SharedPreferences sharedPreferences_qr;
    private String TAG="TradeListFragment";

    private Button btn_mypage;
    private Fragment fragment_mypage;
    private RecyclerView rv_trade_list;
    private TradeListAdapter trade_adapter;
    boolean isLoading = false;
    static final ArrayList<ListItem> itemlist = new ArrayList<ListItem>();
    static final ArrayList<ListItem> templist = new ArrayList<ListItem>();

    private PopupWindow popupWindow_image_password;
    private ImageButton ib_take_picture;
    private Button btn_submit;
    private EditText et_password_submit;
    private SharedPreferences.Editor sharedPreferences_fragment_move_editor;
    private Fragment fragment_posting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trade_list, container, false);

        btn_mypage = (Button) view.findViewById(R.id.btn_mypage);
        fragment_mypage = new MyPageFragment();
        sharedPreferences_qr = getActivity().getSharedPreferences("setting", Context.MODE_PRIVATE);
        Boolean flag_qr = sharedPreferences_qr.getBoolean("flag_qr",false);
        sharedPreferences_fragment_move_editor = getActivity().getSharedPreferences("setting", Context.MODE_PRIVATE).edit();
        fragment_posting = new PostingFragment();
        rv_trade_list = (RecyclerView)view.findViewById(R.id.rv_trade_list);
        rv_trade_list.setLayoutManager(new LinearLayoutManager(getActivity()));

        // 권한 요청 받기
        this.checkPermissions();

        this.GetTradeList();
        this.initAdapter();
        this.initScrollListener();
        this.ToMyPage();

        if(flag_qr == true){
            // 리스트뷰의 아이템들을 누르면 이미지와 비밀번호를 등록하도록 나옴
            Log.w(TAG,"QR 찍고 거래내역 출력");
            this.SetItemForPost();
        }
        else if(flag_qr == false){
            // 리스트뷰의 아이템들을 누르면 해당하는 게시물로 이동
            Log.w(TAG,"마이페이지에서 거래내역 출력");
            this.SetItemToBoard();
        }

        // Inflate the layout for this fragment
        return view;
    }

    public void checkPermissions() {
        String permission_camera = Manifest.permission.CAMERA;
        String[] permissions = {permission_camera};

        int PERMISSION_ALL = 1;

        // checkSelfPermission이 API 23부터 가능, 마시멜로우(Build.VERSION_CODES.M)이 API 23임
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(permission_camera) == PackageManager.PERMISSION_GRANTED) {
                Log.w(TAG, "권한 설정 완료");
            } else {
                Log.w(TAG, "권한 설정 요청");
                ActivityCompat.requestPermissions(getActivity(), permissions, PERMISSION_ALL);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // permissions[0] == Manifest.permission.CAMERA , permissions[1] == Manifest.permission.WRITE_EXTERNAL_STORAGE
        // grantResults[0] => permissions[0]의 권한에 대한 결과 , grantResults[1] => permissions[1]의 권한에 대한 결과
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.w(TAG, "onRequestPermissionsResult");

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            // PERMISSION_GRANTED == 0 , PERMISSION_DENIED == -1
            Log.w(TAG, "Permission : " + permissions[0] + " was " + grantResults[0]);
            Log.w(TAG, "Permission : " + permissions[1] + " was " + grantResults[1]);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        try {
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO:
                    if (resultCode == RESULT_OK) {
                        Log.w(TAG, "사진 파일 생성");
                        Log.w(TAG, "str_CurrentPhotoPath : " + str_CurrentPhotoPath); // 해당 파일의 경로
                        File file_from_path = new File(str_CurrentPhotoPath);
                        Bitmap bitmap = (Bitmap) MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), Uri.fromFile(file_from_path));

                        if (bitmap != null) {
                            ib_take_picture.setImageBitmap(bitmap);
                        }
                    } else if (resultCode == RESULT_CANCELED) {
                        Log.w(TAG, "사진 파일 생성 실패");
                        str_CurrentPhotoPath = "";
                        Log.w(TAG, "str_CurrentPhotoPath : " + str_CurrentPhotoPath); // 해당 파일의 경로
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 카메라로 찍은 사진을 파일로 만들어주는 함수
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        str_CurrentPhotoPath = image.getAbsolutePath();

        Log.w(TAG, "imageFileName : " + imageFileName + ".jpg"); // 생성된 파일 이름
        Log.w(TAG, "str_CurrentPhotoPath : " + str_CurrentPhotoPath); // 해당 파일의 경로

        return image;
    }

    // 카메라로 사진을 찍는 함수
    private void getImageFromCamera() {
        Intent intent_take_picture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent_take_picture.resolveActivity(getActivity().getPackageManager()) != null) {
            File file_image = null;
            try {
                file_image = createImageFile();
            } catch (Exception e) {
                Log.w(TAG, "그림 파일 만드는 도중 에러 발생");
            }
            if (file_image != null) {
                Uri uri_image = FileProvider.getUriForFile(getActivity(), "com.example.login.fileprovider", file_image);

                intent_take_picture.putExtra(MediaStore.EXTRA_OUTPUT, uri_image);
                startActivityForResult(intent_take_picture, REQUEST_TAKE_PHOTO);
            }
        }
    }

    // todo : url 임시 (status 추가 필요)
    public void GetTradeList(){
        Log.w(TAG,"GetTradeList() 함수 실행");
        NetworkTask networkTask = new NetworkTask(getActivity().getApplicationContext(),"http://3.35.48.170:3000/trade/list?type=false&tradeTime=2020-01-01","GET"); // true가 구매리스트, false가 판매리스트
        try{
            //{"msg":"success","tradeVo":{"tradeId":1,"buyerId":"bmh1211@gmail.com","sellerId":"jae961217@naver.com","boardId":"1","tradeTime":"2020-12-23T00:00:00.000+00:00","boardTitle":null}}
            JSONObject resultObject = new JSONObject(networkTask.execute().get());

            if(resultObject == null)
            {
                Log.e(TAG,"연결실패");
                return;
            }

            String resultString = resultObject.getString("msg");

            if(resultString.equals("failed")){
                Toast.makeText(getActivity(),resultString, Toast.LENGTH_SHORT).show();
            }
            else if(resultString.equals("success")){
                Toast.makeText(getActivity(),resultString, Toast.LENGTH_SHORT).show();

                JSONObject tradeObject = resultObject.getJSONObject("tradeVo");
                Log.w(TAG,tradeObject.toString());

                String title, tradeTime, userID;
                int boardId;
                title = tradeObject.getString("title");
                tradeTime = tradeObject.getString("tradeTime").substring(0,10);
                userID = tradeObject.getString("buyerId");
                boardId = tradeObject.getInt("boardId");

                ListItem temp = new ListItem();
                temp.setTitle(title);
                temp.setTradeTime(tradeTime);
                temp.setUserID(userID);
                temp.setBoardId(boardId);

                for(int i=0;i<30;i++){
                    // 처음 받아온 데이터 한번에 itemlist에 넣음
                    // todo : 50은 나중에 tradeVo의 사이즈로 변경
                    itemlist.add(temp);
                }

                // 처음 출력할 아이템
                if(itemlist.size()<10){
                    for(int i=0;i<itemlist.size();i++){
                        templist.add(itemlist.get(i));
                    }
                }
                else{
                    for(int i=0;i<10;i++){
                        templist.add(itemlist.get(i));
                    }
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void SetItemToBoard(){
        trade_adapter.setOnItemClickListener(new TradeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                sharedPreferences_fragment_move_editor.putString("fragment_move","TradeListFragment").commit();
                
                // 게시물 제목, 등록 날짜, 등록자 같은 게시물로 이동
                ListItem temp = trade_adapter.listviewitem.get(position);
                String title = temp.getTitle();
                String tradeTime = temp.getTradeTime();
                String userID = temp.getUserID();
                int boardId = temp.getBoardId();

                Log.w(TAG,"SetItemToBoard( title : "+title+", tradeTime : "+tradeTime+", userID : "+userID+", boardId : "+boardId+" )");

                // bundle을 통해 클릭된 게시물의 boardId를 보냄
                Bundle bundle = new Bundle();
                bundle.putInt("boardId",boardId);
                fragment_posting.setArguments(bundle);

                // 프래그먼트로 이동 -> 현재는 테스트용으로 fragment_mypage 로 지정해놓음
                ((MainPageActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment_posting).commit();
            }
        });
    }

    public void SetItemForPost(){
        trade_adapter.setOnItemClickListener(new TradeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                // 팝업창이 들어갈 뷰를 하나 생성해주고, 해당 뷰의 레이아웃을 LinearLayout 으로 지정
                View popupView = getLayoutInflater().inflate(R.layout.popupwindow_image_password,null);
                popupWindow_image_password=new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

                // 외부영역 선택시 PopUp창 사라짐
                popupWindow_image_password.setFocusable(true);

                // 팝업창의 위치를 디스플레이의 중앙에 위치시킴
                popupWindow_image_password.showAtLocation(popupView, Gravity.CENTER,0,0);

                // 팝업창에 들어갈 객체들 선언
                ib_take_picture = (ImageButton) popupWindow_image_password.getContentView().findViewById(R.id.ib_take_pictrue);
                et_password_submit = (EditText)  popupWindow_image_password.getContentView().findViewById(R.id.et_password_submit);
                btn_submit = (Button) popupWindow_image_password.getContentView().findViewById(R.id.btn_submit);

                ib_take_picture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 현재는 버튼을 누르면 사진을 찍도록 되어있지만 qr을 찍어서 인식되면 바로 카메라 켜지도록 변경
                        getImageFromCamera();
                    }
                });

                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(str_CurrentPhotoPath.equals("")){
                            Toast.makeText(getActivity(), "등록 할 사진이 필요합니다", Toast.LENGTH_SHORT).show();
                        }
                        else if(et_password_submit.getText().toString().equals("")){
                            Toast.makeText(getActivity(), "비밀번호를 등록해주세요", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String password_submit=et_password_submit.getText().toString();
                            //sendImageFile(password_submit, str_CurrentPhotoPath);
                        }
                    }
                });
            }
        });
    }

    // 만약 비밀번호와 사진을 같이 보낸다면 이 함수 사용(url 수정 필요), 사진만 보낸다면 jsonObject에서 비밀번호 들어가는 부분 제거
    // sendImageFile(send_title,send_posting,date_posting,10000,str_CurrentPhotoPath);
    public void sendImageFile(String password, String path) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // todo : url 수정, 성공시 바뀐 msg로 수정
        NetworkTask fileNetworkTask = new NetworkTask(((MainPageActivity) getActivity()).getApplicationContext(), "http://3.35.48.170:3000/", jsonObject, path, "FILE");

        try {
            JSONObject resultFileObject = new JSONObject(fileNetworkTask.execute().get());
            // resultFileObjet = "{"msg":"Register success"}"
            System.out.println(resultFileObject);

            if (resultFileObject.getString("msg").equals("Register success")) {
                Toast.makeText(getActivity(), resultFileObject.getString("msg"), Toast.LENGTH_SHORT).show();
            } else if (resultFileObject == null) {
                Log.w(TAG, "연결 실패");
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initAdapter(){
        trade_adapter = new TradeListAdapter(templist);
        rv_trade_list.setAdapter(trade_adapter);
    }

    public void initScrollListener(){
        rv_trade_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d(TAG, "onScrollStateChanged : " + newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d(TAG, "onScrolled : dx " + dx + ", dy " + dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == templist.size() - 1) {
                        // 리스트의 마지막일 경우
                        dataMore();
                        isLoading = true;
                        Toast.makeText(getActivity(), "스크롤 맨 밑 감지", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void dataMore(){
        Log.w(TAG, "dataMore : ");

        // 로딩 아이템을 추가하고(빈칸 하나) 추가 알림
        templist.add(null);
        trade_adapter.notifyItemInserted(templist.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 추가되어있던 로딩 아이템(null)을 하나 제거하고
                templist.remove(templist.size() - 1);
                // 제거 알림 => 하나를 제거했으므로 list.size()가 이전의 list.size()-1 과 같은 값을 가짐
                trade_adapter.notifyItemRemoved(templist.size());

                int currentSize = templist.size();
                int nextLimit = currentSize + 10;

                // 아이템이 하나 제거 된 위치부터 10개를 추가할 리스트에 넣어줌
                for (int i = currentSize; i < nextLimit; i++) {
                    if (i == itemlist.size())
                        return;

                    templist.add(itemlist.get(i));
                }

                trade_adapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);
    }

    public void ToMyPage(){
        btn_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment_mypage).commit();
            }
        });
    }
}