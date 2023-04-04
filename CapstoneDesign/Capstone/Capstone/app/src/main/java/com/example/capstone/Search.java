package com.example.capstone;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


public class Search extends Fragment {
    Button domestic_btn , overseas_btn;

    //국산 수입 Fragment
    Search_frag_L fragL;
    Search_frag_R fragR;

    //텍스트뷰
    TextView firstshow;

    //네트워크 상태 받는 변수
    private int status;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //뷰를 가져온다. (findviewmyid 사용)
        View layout = inflater.inflate(R.layout.search, container, false);
        //getApplicationContext대신해서 사용
        Context context = container.getContext();

        //툴바 22.03.17
        ((MainActivity) getActivity()).setTitle("검색하기");
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김


        //프레그먼트 생성
        fragL = new Search_frag_L();
        fragR = new Search_frag_R();

        //국산 수입 버튼
        domestic_btn = layout.findViewById(R.id.domestic);
        overseas_btn = layout.findViewById(R.id.overseas);

//        //툴바설정
//        Toolbar toolbar = layout.findViewById(R.id.toolbar_search);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //"국산 \u0026 수입 버튼을 클릭 해 주세요." 텍스트
        firstshow = layout.findViewById(R.id.search_select);

        domestic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstshow.setVisibility(View.GONE);
                domestic_btn.setBackgroundResource(R.drawable.btn_domestic_click);
                overseas_btn.setBackgroundResource(R.drawable.btn_overseas);
                status = NetworkStatus.getConnectivityStatus(context);

                getChildFragmentManager().beginTransaction().replace(R.id.search_main_frame, fragL).commit();
//                if(status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI){
//                    getSupportFragmentManager().beginTransaction().replace(R.id.search_main_frame, fragL).commit();
//                }
//                else{
//                    show();
//                }
            }
        });
        overseas_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstshow.setVisibility(View.GONE);
                domestic_btn.setBackgroundResource(R.drawable.btn_domestic);
                overseas_btn.setBackgroundResource(R.drawable.btn_overseas_click);
                status = NetworkStatus.getConnectivityStatus(context);

                getChildFragmentManager().beginTransaction().replace(R.id.search_main_frame, fragR).commit();
//                if(status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI){
//                    getSupportFragmentManager().beginTransaction().replace(R.id.search_main_frame, fragR).commit();
//                }
//                else{
//                    show();
//                }
            }
        });
        return layout;
    }
}