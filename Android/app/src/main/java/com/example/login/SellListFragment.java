package com.example.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.login.Data.ListItem;
import com.example.login.adapter.TradeListAdapter;
import com.example.login.network.NetworkTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SellListFragment extends Fragment {
    private String TAG = "SellListFragment";
    Fragment fragment_board;

    private Button btn_mypage;
    Fragment fragment_mypage;
    static final ArrayList<ListItem> itemlist = new ArrayList<ListItem>();
    static final ArrayList<ListItem> templist = new ArrayList<ListItem>();
    private TradeListAdapter sell_adapter;
    private boolean isLoading = false;
    private RecyclerView rv_sell_list;

    private SharedPreferences.Editor sharedPreferences_fragment_move_editor;
    private Fragment fragment_posting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sell_list, container, false);

        btn_mypage = (Button)view.findViewById(R.id.btn_mypage);
        fragment_board = new BoardFragment();
        fragment_mypage = new MyPageFragment();
        rv_sell_list=(RecyclerView) view.findViewById(R.id.rv_sell_list);
        rv_sell_list.setLayoutManager(new LinearLayoutManager(getActivity()));

        sharedPreferences_fragment_move_editor = getActivity().getSharedPreferences("setting", Context.MODE_PRIVATE).edit();
        fragment_posting = new PostingFragment();

        // 게시판 데이터 까기 테스트
        this.GetSellPost();
        this.initAdapter();
        this.initScrollListener();

        this.SetItemToBoard();
        this.ToMyPage();

        return view;
    }

    public void GetSellPost()
    {
        Log.w(TAG,"GetSellPost() 함수 실행");

        NetworkTask networkTask = new NetworkTask(getActivity().getApplicationContext(),"http://3.35.48.170:3000/trade/list?type=false&tradeTime=2020-01-01","GET"); // true가 구매리스트, false가 판매리스트
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

                JSONObject sellObject = resultObject.getJSONObject("tradeVo");
                Log.w("SellListFragment",sellObject.toString());

                String title, tradeTime, userID;
                int boardId;
                title = sellObject.getString("title");
                tradeTime = sellObject.getString("tradeTime").substring(0, 10);
                userID = sellObject.getString("buyerId");
                boardId = sellObject.getInt("boardId");

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
        } catch (
                ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void initAdapter(){
        sell_adapter = new TradeListAdapter(templist);
        rv_sell_list.setAdapter(sell_adapter);
    }

    public void initScrollListener(){
        rv_sell_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        sell_adapter.notifyItemInserted(templist.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 추가되어있던 로딩 아이템(null)을 하나 제거하고
                templist.remove(templist.size() - 1);
                // 제거 알림 => 하나를 제거했으므로 list.size()가 이전의 list.size()-1 과 같은 값을 가짐
                sell_adapter.notifyItemRemoved(templist.size());

                int currentSize = templist.size();
                int nextLimit = currentSize + 10;

                // 아이템이 하나 제거 된 위치부터 10개를 추가할 리스트에 넣어줌
                for (int i = currentSize; i < nextLimit; i++) {
                    if (i == itemlist.size())
                        return;

                    templist.add(itemlist.get(i));
                }

                sell_adapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);
    }

    public void SetItemToBoard(){
        sell_adapter.setOnItemClickListener(new TradeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                sharedPreferences_fragment_move_editor.putString("fragment_move","SellListFragment").commit();

                // 게시물 제목, 등록 날짜, 등록자 같은 게시물로 이동
                ListItem temp = sell_adapter.listviewitem.get(position);
                String title = temp.getTitle();
                String tradeTime = temp.getTradeTime();
                String userID = temp.getUserID();
                int boardId = temp.getBoardId();

                Log.w(TAG,"SetItemToBoard( title : "+title+", tradeTime : "+tradeTime+", userID : "+userID+", boardId : "+boardId+" )");

                // bundle을 통해 클릭된 게시물의 boardId를 보냄
                Bundle bundle = new Bundle();
                bundle.putInt("boardId",boardId);
                fragment_posting.setArguments(bundle);

                // 프래그먼트로 이동 -> 현재는 테스트용으로 fragment1 으로 지정해놓음
                ((MainPageActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment_posting).commit();
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