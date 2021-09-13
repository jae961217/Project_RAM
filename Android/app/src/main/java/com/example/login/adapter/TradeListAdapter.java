package com.example.login.adapter;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.Data.ListItem;
import com.example.login.MainPageActivity;
import com.example.login.R;

import java.util.ArrayList;

public class TradeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public ArrayList<ListItem> listviewitem;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private String TAG = "TradeListAdapter";
    private OnItemClickListener mListener = null;
    private SharedPreferences.Editor sharedPreferences_fragment_move_editor;

    public TradeListAdapter(ArrayList<ListItem> itemList) {
        listviewitem = itemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // ViewHolder가 생성되는 함수 => 여기서 ViewHolder객체를 만들어줌 (ViewHolder => View 객체를 담고 있는 것)

        if (viewType == VIEW_TYPE_ITEM) {
            Log.w(TAG,"onCreateViewHolder(VIEW_TYPE_ITEM)");

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_theme, parent, false);
            return new ItemViewHolder(view);
        }

        // 새로운 아이템 로딩일 때 나타나는 부분
        else if(viewType == VIEW_TYPE_LOADING){
            Log.w(TAG,"onCreateViewHolder(VIEW_TYPE_LOADING)");

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_loading, parent, false);
            return new LoadingViewHolder(view);
        }

        else{
            Log.w(TAG,"onCreateViewHolder(NOTHING)");

            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        // 생성된 ViewHolder에 데이터를 바인딩 해주는 함수
        // 데이터가 스크롤 되어서 맨 위에있던 ViewHolder 객체가 맨 아래로 이동한다면, 그 레이아웃은 재사용하되 데이터는 새롭게 바뀌어야함
        // 아래에서 새롭게 보여질 데이터의 인덱스값이 position

        // 참조변수 instanceof 타입(클래스명) => true/false => true는 참조변수가 타입(클래스명)으로 형변환이 가능하다는 말
        if (viewHolder instanceof ItemViewHolder) {
            Log.w(TAG,"onBindViewHolder(ItemViewHolder)");

            populateItemRows((ItemViewHolder) viewHolder, position);
        }
        else if (viewHolder instanceof LoadingViewHolder) {
            Log.w(TAG,"onBindViewHolder(LoadingViewHolder)");

            showLoadingView((LoadingViewHolder) viewHolder, position);
        }
    }

    @Override
    public int getItemCount() {
        // 비어있다면 0, 그렇지 않다면 사이즈 리턴
        return listviewitem == null ? 0 : listviewitem.size();
    }

    @Override
    public int getItemViewType(int position) {
        // position(인덱스)가 존재하지않는다면 Loading, 인덱스가 존재한다면 item 객체라는 것
        return listviewitem.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        // ItemViewHolder 클래스 정의
        TextView tv_title;
        TextView tv_buyer_seller_id;
        TextView tv_tradeTime;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);
            tv_buyer_seller_id = itemView.findViewById(R.id.tv_buyer_seller_id);
            tv_tradeTime = itemView.findViewById(R.id.tv_tradeTime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onItemClick(v, pos);
                        }
                    }
                }
            });
        }
    }


    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        // LoadingViewHolder 클래스 정의

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        // progressBar가 나타날때 해줄 작업 여기에 작성

        Log.w(TAG,"showLoadingView");
    }

    private void populateItemRows(ItemViewHolder viewHolder, int position) {
        // 아이템이 재활용될 때 객체 안의 데이터를 변경해주는 기능

        ListItem item = listviewitem.get(position);
//        viewHolder.tv_item.setText(item);
        viewHolder.tv_title.setText(item.getTitle());
        viewHolder.tv_buyer_seller_id.setText(item.getUserID());
        viewHolder.tv_tradeTime.setText(item.getTradeTime().substring(0,10));

        Log.w(TAG,"populateItemRows(title : "+item.getTitle()+", userId : "+item.getUserID()+", tradeTime : "+item.getTradeTime().substring(0,10)+")");
    }

    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }
}
// arraydapter와 listview 사용하기 : https://armful-log.tistory.com/26

// =========RecyclerViewAdapter===========
// https://yujin-dev.tistory.com/35
// https://class-programming.tistory.com/34
// https://recipes4dev.tistory.com/168