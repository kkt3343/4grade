package com.example.capstone;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.util.Log;
import android.view.Gravity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {

    //비트맵
    private Bitmap bitmap;
    //이미지 사용
    protected ArrayList<ImageView> imagebox = new ArrayList<>();
    //버튼
    private Button main_searchbutton;
    //이미지 주소
    protected ArrayList<String> box = new ArrayList<>();

    private BottomNavigationView bottomNavigationView; // 바텀네비게이션 뷰

    private long backpressedTime = 0;
    String mytoken = "";

    //네트워크 상태 받는 변수
    private int status;

    //하단바 프래그먼트
    Home bottom1;
    History bottom2;
    Favorite bottom3;
    Search bottom11;
    Search_ResultActivity bottom111;
    Search_ResultActivity_keyword search_resultActivity_keyword;


    //프래그먼트
    FragmentManager fragmentManager;

    String search_result_activity_name = "";
    String search_result_activity_keyword_name = "";
    int bottomnagation_postion = 1;

    //툴바
    Menu mMenu;

    //로딩화면
    ProgressDialog customProgressDialog;
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        setTitle("메인화면");
        super.onCreate(saveInstanceState);
        setContentView(R.layout.main);

        //툴바 22.03.10
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(false); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        //캐시에 저장된 mytoken을 가져온다.
        mytoken = PreferenceManager.getString(getApplicationContext() ,"token");
        //저장된 값이 없다면 토큰을 발생시킨다.
        if(mytoken.equals(""))
        {
            //FCM으로 token값을 발생시키고 저장한다.
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                return;
                            }

                            // Get new FCM registration token
                            String token = task.getResult();

                            // Log and toast
                            String msg = getString(R.string.msg_token_fmt, token);
                            mytoken = token;

                            //단 한번만 실행
                            FCM_token tn = new FCM_token();
                            String tnaddress ="http://" + getString(R.string.net_ip) + "/token_test.php?token="+ mytoken;
                            Log.d("myadress",tnaddress);

                            tn.site = tnaddress;
                            tn.start();
                            try {
                                tn.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            Log.d("tokenaddress",token);
                            Toast.makeText(getApplicationContext(),"토큰주소:" + token,Toast.LENGTH_SHORT).show();

                            //Preference에 토큰 정보 저장
                            PreferenceManager.setString(getApplicationContext() ,"token", token);
                        }
                    });
        }


        //로딩창 객체 생성 22.04.08
        customProgressDialog = new ProgressDialog(getApplicationContext());
        //로딩창을 투명하게
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        //처음에는 메인화면 설정
        fragmentManager = getSupportFragmentManager();

        //완전연결실패
        status = NetworkStatus.getConnectivityStatus(getApplicationContext());
        if(status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI){
            bottom1 = new Home();
            fragmentManager.beginTransaction().replace(R.id.main_frame, bottom1).commit();
        }else{
            disconnected();
        }


        //하단바 동작
        bottomNavigationView = findViewById(R.id.bottom_menu);
        bottomNavigationView.getMenu().getItem(3).setCheckable(false);
        bottomNavigationView.getMenu().getItem(4).setCheckable(false);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //알림 & 메뉴
                Intent nextIntent;
                //네트워크 상태 확인
                int status;
                switch (item.getItemId()){
                    case R.id.first_tab:
                        //메인화면
                        setTitle("메인화면");
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                        status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                        if(status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI){
                            if(bottom1==null){
                                fragmentManager.beginTransaction().add(R.id.main_frame, bottom1).commit();
                            }
                            if(bottom1!=null) {
                                if(bottom11!=null){
                                    if(bottom111!=null){
                                        setTitle(search_result_activity_name);
                                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                        fragmentManager.beginTransaction().show(bottom111).commit();
                                    }
                                    else{
                                        setTitle("검색하기");
                                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                        fragmentManager.beginTransaction().show(bottom11).commit();
                                        //item.setTitle("홈 화면으로");
                                    }
                                }
                                else if(search_resultActivity_keyword!=null){
                                    setTitle(search_result_activity_keyword_name);
                                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                    fragmentManager.beginTransaction().show(search_resultActivity_keyword).commit();
                                }
                                else{
                                    fragmentManager.beginTransaction().show(bottom1).commit();
                                    //item.setTitle("메인으로");
                                }
                            }
                            if(bottom2!=null) fragmentManager.beginTransaction().hide(bottom2).commit();
                            if(bottom3!=null) fragmentManager.beginTransaction().hide(bottom3).commit();
                        }
                        else{
                            show();
                        }
                        break;
                    case R.id.second_tab:
                        //먼저 네트워크 연결을 확인한다.
                        status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                        if(status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI){
                            //성공했다면 서버와 연결을 확인한다.
                            //검색기록
                            bottomnagation_postion = 2;
                            setTitle("검색 기록");
                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                            if(bottom2==null){
                                bottom2 = new History();
                                fragmentManager.beginTransaction().add(R.id.main_frame, bottom2).commit();
                            }
                            if(search_resultActivity_keyword!=null) fragmentManager.beginTransaction().hide(search_resultActivity_keyword).commit();
                            if(bottom1!=null) fragmentManager.beginTransaction().hide(bottom1).commit();
                            if(bottom11!=null) fragmentManager.beginTransaction().hide(bottom11).commit();
                            if(bottom111!=null) fragmentManager.beginTransaction().hide(bottom111).commit();
                            if(bottom2!=null) fragmentManager.beginTransaction().show(bottom2).commit();
                            if(bottom3!=null) fragmentManager.beginTransaction().hide(bottom3).commit();
                        }
                        else{
                            show();
                            return false;
                        }
                        break;
                    case R.id.third_tab:
                        //먼저 네트워크 연결을 확인한다.
                        status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                        if(status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI){
                            //성공했다면 서버와 연결을 확인한다.
                            setTitle("찜 목록");
                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                            if(bottom3==null){
                                bottom3 = new Favorite();
                                fragmentManager.beginTransaction().add(R.id.main_frame, bottom3).commit();
                            }
                            if(search_resultActivity_keyword!=null) fragmentManager.beginTransaction().hide(search_resultActivity_keyword).commit();
                            if(bottom1!=null) fragmentManager.beginTransaction().hide(bottom1).commit();
                            if(bottom11!=null) fragmentManager.beginTransaction().hide(bottom11).commit();
                            if(bottom111!=null) fragmentManager.beginTransaction().hide(bottom111).commit();
                            if(bottom2!=null) fragmentManager.beginTransaction().hide(bottom2).commit();
                            if(bottom3!=null) fragmentManager.beginTransaction().show(bottom3).commit();
                        }
                        else{
                            show();
                            return false;
                        }
                        break;
                    case R.id.fourth_tab:
                        //알림
                        status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                        //네트워크 연결 확인
                        if(status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI){
                            nextIntent = new Intent(getApplicationContext(), Notice.class);
                            startActivity(nextIntent);
                            return false;
                        }
                        //네트워크 연결 실패
                        else{
                            show();
                            return false;
                        }
                    case R.id.fifth_tab:
                        nextIntent = new Intent(getApplicationContext(), User_Menu.class);
                        startActivity(nextIntent);
                        return false;
                }
                //return ture 일시 하단 버튼이 checked 됨
                return true;
            }
        });
    }

    //연결확인하기
    //참조 : https://mommoo.tistory.com/52
    boolean isOnline() {
        CheckConnect cc = new CheckConnect("http://" + getString(R.string.net_ip));
        int requestcode;
        cc.start();
        try{
            cc.join();
            requestcode = cc.getint();
            return cc.isSuccess();
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    
    //인터넷 연결 실패시 보여줄 화면
    //참조 : https://webnautes.tistory.com/1094
    void show()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("연결 실패");
        builder.setMessage("인터넷 연결이 되어있지 않거나\n서버와 연결을 할 수 없습니다.");
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //앱 자체를 종료
                        //finishAffinity();
                        //System.runFinalization();
                        //System.exit(0);
                    }
                });
//        builder.setNegativeButton("아니오",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
//                    }
//                });
        builder.setCancelable(false);
        builder.show();
    }

    //완전 연결 실패
    void disconnected()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("연결 실패");
        builder.setMessage("인터넷 연결이 되어있지 않거나\n서버와 연결을 할 수 없습니다.\n앱을 종료합니다.");
        builder.setPositiveButton("종료",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //앱 자체를 종료
                        finishAffinity();
                        System.runFinalization();
                        System.exit(0);
                    }
                });
        builder.setCancelable(false);
        builder.show();
    }

    //종료하기 두번 클릭
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backpressedTime + 2000) {
            backpressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() <= backpressedTime + 2000) {
            finish();
        }
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.actions_menu, menu);
        mMenu = menu;
        //mMenu.findItem(R.id.sort_menu).setVisible(false);
        return true;
    }

    //Toolbar 뒤로가기
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:{
                if(bottom111!=null){
                    setTitle("검색하기");
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    fragmentManager.beginTransaction().show(bottom11).commit();
                    fragmentManager.beginTransaction().hide(bottom111).commit();
                    fragmentManager.beginTransaction().remove(bottom111).commit();
                    bottom111 = null;
                }
                else if(search_resultActivity_keyword!=null){
                    setTitle("메인화면");
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    fragmentManager.beginTransaction().show(bottom1).commit();
                    fragmentManager.beginTransaction().hide(search_resultActivity_keyword).commit();
                    fragmentManager.beginTransaction().remove(search_resultActivity_keyword).commit();
                    search_resultActivity_keyword = null;
                }
                else{
                    if(bottom11!=null) {
                        setTitle("메인화면");
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        fragmentManager.beginTransaction().hide(bottom11).commit();
                        fragmentManager.beginTransaction().show(bottom1).commit();
                        fragmentManager.beginTransaction().remove(bottom11).commit();
                        bottom11 = null;
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

