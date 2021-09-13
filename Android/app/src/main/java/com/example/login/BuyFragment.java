package com.example.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BuyFragment extends Fragment {
    private EditText et_alarm_password_check;
    private Button btn_alarm_submit;
    private String password_from_server="1234"; // 임시 비밀번호

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy, container, false);

        et_alarm_password_check = (EditText) view.findViewById(R.id.et_alarm_password_check);
        btn_alarm_submit = (Button) view.findViewById(R.id.btn_alarm_submit);

        btn_alarm_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo : 서버암호 일치여부 확인
                if(et_alarm_password_check.getText().toString().equals(password_from_server)){
                    // 비밀번호가 일치하는 경우
                    Toast.makeText(getActivity(), "비밀번호가 일치합니다", Toast.LENGTH_SHORT).show();
                }
                else{
                    // 비밀번호가 일치하지 않는 경우
                    Toast.makeText(getActivity(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                    et_alarm_password_check.setText(null);
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}