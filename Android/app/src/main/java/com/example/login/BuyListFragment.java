package com.example.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.Data.ListItem;
import com.example.login.adapter.TradeListAdapter;
import com.example.login.network.NetworkTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class BuyListFragment extends Fragment {
    private String TAG = "BuyListFragment";
    private Button btn_mypage;
    Fragment fragment_mypage;
    private TradeListAdapter buy_adapter;
    RecyclerView rv_buy_list;
    static final ArrayList<ListItem> itemlist = new ArrayList<ListItem>();
    static final ArrayList<ListItem> templist = new ArrayList<ListItem>();
    private boolean isLoading = false;

    // 구매내역에서 판매자정보 팝업윈도우에 필요한 데이터들
    private PopupWindow ll_sellerInfo;
    private TextView tv_sellerName;
    private TextView tv_price;
    private TextView tv_location;
    private TextView tv_evalPoint;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy_list, container, false);

        btn_mypage = (Button)view.findViewById(R.id.btn_mypage);
        fragment_mypage = new MyPageFragment();
        rv_buy_list=(RecyclerView) view.findViewById(R.id.rv_buy_list);
        rv_buy_list.setLayoutManager(new LinearLayoutManager(getActivity()));

        // 구매 내역 받아옴
        this.GetBuyPost();
        this.initAdapter();
        this.initScrollListener();
        this.SetItemClick();
        this.ToMyPage();

        return view;
    }

    public void showSellerInfo(){
        // 팝업창이 들어갈 뷰를 하나 생성해주고, 해당 뷰의 레이아웃을 LinearLayout 으로 지정
        View popupView = getLayoutInflater().inflate(R.layout.popupwindow_seller_info,null);
        ll_sellerInfo=new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        // 외부영역 선택시 PopUp창 사라짐
        ll_sellerInfo.setFocusable(true);

        // 팝업창의 위치를 디스플레이의 중앙에 위치시킴
        ll_sellerInfo.showAtLocation(popupView, Gravity.CENTER,0,0);

        // 팝업창에 들어갈 TextView 객체 선언
        tv_sellerName=(TextView)ll_sellerInfo.getContentView().findViewById(R.id.tv_sellerName);
        tv_price=(TextView)ll_sellerInfo.getContentView().findViewById(R.id.tv_price);
        tv_location=(TextView)ll_sellerInfo.getContentView().findViewById(R.id.tv_location);
        tv_evalPoint=(TextView)ll_sellerInfo.getContentView().findViewById(R.id.tv_evalPoint);

        // 팝업창에 들어갈 TextView 들의 Text 를 지정해줌
        // todo : 판매자 정보 가져오는 작업 필요
        tv_sellerName.setText("거래 상대 : "+ "상대이름");
        tv_price.setText("가격 : "+ "가격(원)");
        tv_location.setText("거래 위치 : "+"거래위치");
        tv_evalPoint.setText("5.0(평점)");
    }

    public void GetBuyPost()
    {
        Log.w("GetPosting","함수 실행");
        NetworkTask networkTask = new NetworkTask(getActivity().getApplicationContext(),"http://3.35.48.170:3000/trade/list?type=true&tradeTime=2020-01-01","GET"); // true가 구매리스트, false가 판매리스트
        try{
            //{"msg":"success","tradeVo":{"tradeId":1,"buyerId":"bmh1211@gmail.com","sellerId":"jae961217@naver.com","boardId":"1","tradeTime":"2020-12-23T00:00:00.000+00:00","boardTitle":null}}
            JSONObject resultObject = new JSONObject(networkTask.execute().get());

            if(resultObject == null)
            {
                Log.e("연결결과","연결실패");
                return;
            }

            String resultString = resultObject.getString("msg");

            if(resultString.equals("failed")){
                Toast.makeText(getActivity(),resultString, Toast.LENGTH_SHORT).show();
            }
            else if(resultString.equals("success")){
                Toast.makeText(getActivity(),resultString, Toast.LENGTH_SHORT).show();

                JSONObject buyObject = resultObject.getJSONObject("tradeVo");
                Log.w(TAG, buyObject.toString());

                String title, tradeTime, userID;
                title=buyObject.getString("title");
                tradeTime = buyObject.getString("tradeTime").substring(0,10);
                userID = buyObject.getString("sellerId");

                ListItem temp = new ListItem();
                temp.setTitle(title);
                temp.setTradeTime(tradeTime);
                temp.setUserID(userID);

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
        } catch (
                ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void initAdapter(){
        buy_adapter = new TradeListAdapter(templist);
        rv_buy_list.setAdapter(buy_adapter);
    }

    public void initScrollListener(){
        rv_buy_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        buy_adapter.notifyItemInserted(templist.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 추가되어있던 로딩 아이템(null)을 하나 제거하고
                templist.remove(templist.size() - 1);
                // 제거 알림 => 하나를 제거했으므로 list.size()가 이전의 list.size()-1 과 같은 값을 가짐
                buy_adapter.notifyItemRemoved(templist.size());

                int currentSize = templist.size();
                int nextLimit = currentSize + 10;

                // 아이템이 하나 제거 된 위치부터 10개를 추가할 리스트에 넣어줌
                for (int i = currentSize; i < nextLimit; i++) {
                    if (i == itemlist.size())
                        return;

                    templist.add(itemlist.get(i));
                }

                buy_adapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);
    }

    public void SetItemClick(){
        buy_adapter.setOnItemClickListener(new TradeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                showSellerInfo();
            }
        });
    }

    public void ToMyPage(){
        btn_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainPageActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment_mypage).commit();
            }
        });
    }
}