package com.example.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class SellQrFragment extends Fragment {
//    private IntentIntegrator qrScan;
    private String resultString = "";
    private String TAG = "SellQrFragment";
    private Fragment fragment_trade_list;
    private SharedPreferences sharedPreferences_qr;
    private SharedPreferences.Editor sharedPreferences_qr_editor;
    private Fragment fragment_mypage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sell_qr, container, false);

        // 처음 qr코드 인식 관련
        fragment_trade_list = new TradeListFragment();
        fragment_mypage = new MyPageFragment();
//        qrScan = new IntentIntegrator(getActivity());

        // SharedPreferences로 qr찍었을 때와 마이페이지에서 접근할 때 구분
        sharedPreferences_qr = getActivity().getSharedPreferences("setting", MODE_PRIVATE);
        sharedPreferences_qr_editor = sharedPreferences_qr.edit(); // sharedPreferences_qr을 제어할 editor 설정

        // qr코드 스캔
        this.QRcheck();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        try {
            switch (requestCode) {
                case IntentIntegrator.REQUEST_CODE:
                    if (resultCode == RESULT_OK) {
                        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
                        if (result != null) {
                            if (result.getContents() == null) {
                                //qr 코드가 없는 경우
                                Toast.makeText(getActivity(), "취소되었습니다", Toast.LENGTH_SHORT).show();
                                Log.w(TAG, "NULL");
                            } else {
                                //qr 코드가 존재하는 경우
                                Toast.makeText(getActivity(), "스캔되었습니다", Toast.LENGTH_SHORT).show();
                                Log.w(TAG, result.getContents());

                                try {
                                    // data를 json으로 변환
                                    JSONObject jsonObject = new JSONObject(result.getContents());
                                    Log.w(TAG, jsonObject.toString());

                                    // 스캔 성공해서 Tester에서 MINO를 받아올 경우에 넘어갈 수 있도록
                                    if (jsonObject.getString("msg").equals("Success")) {
                                        resultString = jsonObject.getString("Tester");

                                        if (resultString.equals("MINO")) {
                                            Log.w(TAG, "QR 스캔 성공");

                                            sharedPreferences_qr_editor.putBoolean("flag_qr", true);
                                            sharedPreferences_qr_editor.commit();

                                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment_trade_list).commit();
                                        }
                                    } else {
                                        Log.w(TAG, "QR 스캔 실패");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            super.onActivityResult(requestCode, resultCode, intent);
                        }
                    }

                    else if(resultCode == RESULT_CANCELED){
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment_mypage).commit();
                        Toast.makeText(getActivity(), "취소되었습니다", Toast.LENGTH_SHORT).show();
                    }
                    break;
//                    finish();
//                    onBackPressed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void QRcheck() {
//        qrScan.forSupportFragment(SellQrFragment.this).setPrompt("QR코드를 스캔해주세요");
//        qrScan.forSupportFragment(SellQrFragment.this).setOrientationLocked(false);
//        qrScan.forSupportFragment(SellQrFragment.this).setBeepEnabled(false);
//        qrScan.forSupportFragment(SellQrFragment.this).initiateScan();
//        qrScan.initiateScan();

        IntentIntegrator.forSupportFragment(SellQrFragment.this)
                .setPrompt("QR코드를 스캔해주세요")
                .setOrientationLocked(false)
                .setBeepEnabled(false).initiateScan();
    }
}