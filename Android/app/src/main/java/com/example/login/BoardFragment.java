package com.example.login;

import android.content.Context;
import android.net.Network;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.login.network.NetworkTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

//todo 서버에서 게시글 목록 받아올때 (날짜 이름 제목 id) 받아오기
public class BoardFragment extends Fragment {

    Fragment postingFragment;
    SwipeRefreshLayout swipe_layout_board;
    public static final int REQUEST_CODE1 = 1000;  //리스트 터치
    private Adapter PostingAdapter;
    private String name, title, date, index, region, status;
    private Handler handler;
    private ListView listView;
    private Button btn_write;
    private String TAG = "BoardFragment";
    private int adapterPosition;
    private JSONArray PostingArray;
    Context ct;
    MainPageActivity activity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_board, container, false);  //https://swalloow.tistory.com/87

        ct = container.getContext();
        PostingAdapter = new Adapter();
        listView = (ListView) view.findViewById(R.id.lv_board);
        listView.setAdapter(PostingAdapter);
        postingFragment = new PostingFragment();
        btn_write=(Button)view.findViewById(R.id.btn_write);
        swipe_layout_board = (SwipeRefreshLayout)view.findViewById(R.id.swipe_layout_board);
        GetPosting(ct);
        LoadPosting(ct,PostingArray);

        // 게시물 작성 버튼 클릭시
        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainPageActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container,postingFragment).commit();
            }
        });

        //게시글 눌렀을 시
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index = PostingAdapter.getIndex(position);
                title = "제목";
                name = "이름";
                String content = "내용";
                date = "날짜";
                Toast.makeText(ct ,name,Toast.LENGTH_LONG).show();
                        Bundle bundle = new Bundle();
                        bundle.putString("index", index);
                        bundle.putString("name", name);
                        //bundle.putSerializable() : 객체를 보낼때 사용함
                        activity.fragDataSend(bundle);
            }
        });


        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 리스트뷰 최상단
//                if(!lv_board.canScrollVertically(-1)){
//                }
                if(!listView.canScrollVertically(1)){
                    // TODO : 최하단 스크롤 시 보이는 리스트 추가
                    Toast.makeText((MainPageActivity)getActivity(), "리스트뷰 최하단 스크롤 시도", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        swipe_layout_board.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText((MainPageActivity)getActivity(), "새로고침 정상 작동", Toast.LENGTH_SHORT).show();
                //MainPageActivity.LoadPosting(MainPageActivity.this);
                Log.d("test 받아온 목록","Test");
                // 새로고침 완료
                swipe_layout_board.setRefreshing(false);
            }
        });

        // 새로고침 화살표 한바퀴 돌때마다 각각의 색상으로 나타난다?
        // TODO : 맨 위 하나만나옴 - 알아보기
        swipe_layout_board.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        // Inflate the layout for this fragment
        return view;
    }

    private void GetPosting(Context context)
    {
        NetworkTask networkTask = new NetworkTask(context,"http://3.35.48.170:3000/board/list?index=0","GET");
        try{
            JSONObject PostingObject = new JSONObject(networkTask.execute().get());
            if(PostingObject == null)
            {
                Log.e(TAG,"연결실패");
            }
            else{
                PostingArray = PostingObject.getJSONArray("list");
                Log.d(TAG,PostingArray.toString());
                Log.d(TAG,PostingArray.getJSONObject(0).toString());
                Log.d(TAG,PostingArray.getJSONObject(0).getString("id"));
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

    private void LoadPosting(Context context, JSONArray PostingArray)   //fragment 불릴때 게시글 목록 생성
    {
        if(PostingArray.toString().length()!=0)
        {
            try{
                for(int i = 0;i<PostingArray.length() ;i++) {
                    JSONObject jsonObject = PostingArray.getJSONObject(i);
                    Log.d(TAG,PostingArray.toString());
                    name = jsonObject.getString("nickName");
                    title = jsonObject.getString("title");
                    date = "2020년 1월 7일 21:58";  //데이터 없어서 임시로 넣음
                    index = "1";
                    region = jsonObject.getString("region");
                    if(jsonObject.getString("status")=="false")
                    {
                        status = "판매중";
                    }
                    else
                    {
                        status = "판매완료";
                    }
                    PostingAdapter.addItem(date, name, title, index, region, status);
                    PostingAdapter.notifyDataSetChanged();
                }
            }catch(JSONException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText((MainPageActivity)getActivity(), "게시물이 없음", Toast.LENGTH_SHORT).show();
        }
    }

}
// listview 새로고침 reference : https://medium.com/@bluesh55/android-%EB%8B%B9%EA%B2%A8%EC%84%9C-%EC%83%88%EB%A1%9C%EA%B3%A0%EC%B9%A8-%EA%B0%84%EB%8B%A8%ED%95%98%EA%B2%8C-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0-a42846c14c23

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        ct = context;
//        PostingAdapter = new Adapter();
//        listView = (ListView) view.findViewById(R.id.lv_board);
//        listView.setAdapter(PostingAdapter);
//        postingFragment = new PostingFragment();
//        btn_write=(Button)view.findViewById(R.id.btn_write);
//        swipe_layout_board = (SwipeRefreshLayout)view.findViewById(R.id.swipe_layout_board);
//        LoadPosting(ct);
//
//    }




