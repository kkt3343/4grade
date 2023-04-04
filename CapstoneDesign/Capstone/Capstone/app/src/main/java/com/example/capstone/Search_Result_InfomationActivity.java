package com.example.capstone;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.protobuf.Any;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Search_Result_InfomationActivity extends AppCompatActivity {

    private ArrayList<String> box = new ArrayList<>();

    private ArrayList<TextView> textbox = new ArrayList<>();

    private Button button1;
    private Button button2;

    private Bitmap bitmap;
    private String netip;
    private LinearLayout infomation_image;
    Search_ResultActivity zzim;


    Context mycontext;
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        setTitle("매물 상세보기");
        super.onCreate(saveInstanceState);
        setContentView(R.layout.search_result_infomation);

        mycontext = getApplicationContext();
        
        //아이피주소받아오기
        netip = getString(R.string.net_ip);

        //툴바 22.03.10
        Toolbar toolbar = (Toolbar) findViewById(R.id.search_result_infomation_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김


        infomation_image = findViewById(R.id.infomation_image);
        button1 = (Button)findViewById(R.id.search_result_infomation_button1);
        button2 = (Button)findViewById(R.id.search_result_infomation_button2);

        int[] setfindview = {
                R.id.use1, R.id.use2, R.id.use3,
                R.id.use4, R.id.use5, R.id.use6,
                R.id.use7, R.id.use9,
                R.id.use10, R.id.use11, R.id.use12};

        //제조사 모델명 세부모델명
        //차량번호 가격 주행거리
        //연식 사고여부 사이트
        //색상 연료 배기량

        for(int i=0;i<11;i++){
            textbox.add((TextView) findViewById(setfindview[i]));
        }

        Intent secondIntent = getIntent();
        try{
            String tmp;
            tmp = secondIntent.getStringExtra("url_address"); //url주소 0
            box.add(tmp);
            tmp = secondIntent.getStringExtra("sitetype"); //사이트타입 (ex.엔카) 1
            box.add(tmp);
            tmp = secondIntent.getStringExtra("car_title"); //자동차 전체 이름 2
            box.add(tmp);
            tmp = secondIntent.getStringExtra("carnumber"); //자동차 번호 3
            box.add(tmp);
            tmp = secondIntent.getStringExtra("cartype"); // 자동차 타입 ex(중형차) 4
            box.add(tmp);
            tmp = secondIntent.getStringExtra("manufacturer"); //제조사 5
            box.add(tmp);
            tmp = secondIntent.getStringExtra("model"); //모델 ex 쏘나타 6
            box.add(tmp);
            tmp = secondIntent.getStringExtra("model_detail"); //모델 자세히 ex LF 쏘나타 7
            box.add(tmp);
            tmp = secondIntent.getStringExtra("price"); //가격 8
            box.add(tmp);
            tmp = secondIntent.getStringExtra("distance"); //주행거리 9
            box.add(tmp);
            tmp = secondIntent.getStringExtra("caryear"); //연식 10
            box.add(tmp);
            tmp = secondIntent.getStringExtra("carfuel"); //연료타입 11
            box.add(tmp);
            tmp = secondIntent.getStringExtra("carcolor"); //자동차 색깔 12
            box.add(tmp);
            tmp = secondIntent.getStringExtra("imglink"); //그림링크 13
            box.add(tmp);
            tmp = secondIntent.getStringExtra("car_id"); //자동차_ID 14
            box.add(tmp);
            tmp = secondIntent.getStringExtra("displacement"); //배기량 15
            box.add(tmp);
        }catch (Exception e){
        }

        //검색기록추가
        add_history(box.get(14));

        ExchangeFormat exchangeFormat = new ExchangeFormat();
        ExchangeFormat_1000 exchangeFormat_1000 = new ExchangeFormat_1000();
        String distance_unit = exchangeFormat_1000.unit_change(box.get(9));
        String price_unit = exchangeFormat.unit_change(box.get(8));

        try{
            Thread thread1 = new uThread();
            thread1.start();
            thread1.join();
            //image1.setImageBitmap(bitmap);
            infomation_image.setBackground(new BitmapDrawable(bitmap));

            textbox.get(0).setText(box.get(5));
            textbox.get(1).setText(box.get(6));
            textbox.get(2).setText(box.get(7));
            textbox.get(3).setText(box.get(3));
            textbox.get(4).setText(price_unit+"원");
            textbox.get(5).setText(distance_unit+"KM");
            textbox.get(6).setText(box.get(10));
            textbox.get(7).setText(box.get(1));
            textbox.get(8).setText(box.get(12));
            textbox.get(9).setText(box.get(11));
            textbox.get(10).setText(box.get(15));

        }catch (Exception e){
        }
        //찜버튼
        zzim = new Search_ResultActivity();
        ArrayList<Object> caridArray_favorite;
        caridArray_favorite = zzim.getStringArrayPref(getApplicationContext(), "favorite");

        //찜버튼추가
        Button favorite_button = new Button(getApplicationContext());
        LinearLayout.LayoutParams tmpbutton = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        tmpbutton.width = 100;
        tmpbutton.height = 100;
        tmpbutton.gravity = Gravity.LEFT;
        favorite_button.setLayoutParams(tmpbutton);

        infomation_image.addView(favorite_button);

        Boolean is_favorite = caridArray_favorite.contains(box.get(14));
        if (is_favorite){
            favorite_button.setBackground(getDrawable(R.drawable.heart6));
        }
        else{
            favorite_button.setBackground(getDrawable(R.drawable.heart5));
        }

        favorite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(caridArray_favorite.contains(box.get(14))){
                    Toast.makeText(getApplicationContext(),"찜 목록에서 제거했습니다.",Toast.LENGTH_SHORT).show();
                    favorite_button.setBackground(getDrawable(R.drawable.heart5));
                    //찜목록에 제거
                    caridArray_favorite.remove(box.get(14));
                    Toast.makeText(getApplicationContext(),"삭제번호:" + box.get(14).toString(), Toast.LENGTH_SHORT).show();
                    zzim.setStringArrayPref(getApplicationContext(), "favorite", caridArray_favorite);
                }
                else{
                    Toast.makeText(getApplicationContext(),"찜 목록에 추가했습니다.",Toast.LENGTH_SHORT).show();
                    favorite_button.setBackground(getDrawable(R.drawable.heart6));
                    //찜목록에 추가
                    caridArray_favorite.add(box.get(14));
                    zzim.setStringArrayPref(getApplicationContext(), "favorite", caridArray_favorite);
                }
            }
        });


        //텍스트 설정
        TextView infomation1 = findViewById(R.id.infomation_1);
        TextView infomation2 = findViewById(R.id.infomation_2);
        TextView infomation3 = findViewById(R.id.infomation_3);
        TextView infomation4 = findViewById(R.id.infomation_4);

        //텍스트 부분 설정

        //폰트설정
        AssetManager assets = getResources().getAssets() ;
        Typeface typeface = Typeface.createFromAsset(assets, "nexon1.otf");

        infomation1.setTypeface(typeface);
        infomation2.setTypeface(typeface);
        infomation3.setTypeface(typeface);

        infomation1.setTextSize(18f);
        infomation2.setTextSize(15f);
        infomation3.setTextSize(20f);

        infomation1.setTextColor(Color.BLACK);

        infomation3.setTextColor(Color.RED);
        infomation3.setTypeface(Typeface.DEFAULT_BOLD);

        infomation1.setText(box.get(2));
        infomation2.setText(box.get(10) + " / " + distance_unit + "KM / " + box.get(11));
        infomation3.setText(price_unit + "원");
        infomation4.setText("<세부 정보>");

        //매물 사이트
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp = box.get(0);
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(tmp)); startActivity(intent);
            }
        });

        //가격비교
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(getApplicationContext(), Detail.class);
                //Detail 부분에 값을 전달해주기
                nextIntent.putStringArrayListExtra("old_sale",box);
                startActivity(nextIntent);
            }
        });
    }
    //그림 가져오기
    class uThread extends Thread{
        public void run(){
            String tmp = "http://"+ netip +"/" + box.get(13);
            try{
                URL url = new URL(tmp);
                HttpURLConnection conn = null;
                conn = (HttpURLConnection) url.openConnection();

                //접속오류시 연결안함
                conn.setConnectTimeout(10000);

                conn.connect();
                try{
                    //연결 성공, 이미지인경우
                    InputStream aaa = conn.getInputStream(); //inputStream 값 가져오기
                    bitmap = BitmapFactory.decodeStream(aaa); // Bitmap으로 반환
                }catch (Exception e){
                    //연결은 성공했지만, 이미지가 아닌경우입니다.
                }
            }catch (Exception SocketTimeoutException){

            }
        }
    }

    //Toolbar 뒤로가기
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    ArrayList<Object> caridArray_history = new ArrayList<>();

    public void add_history(String car_id)
    {
        _get_setArraypref pref = new _get_setArraypref();
        caridArray_history = pref.getStringArrayPref(mycontext, "history");

        //최상단으로 옮기기
        if (caridArray_history.contains(car_id)) {
            caridArray_history.remove(car_id);
        }
        //검색기록 저장 (History 개수가 20개 이상이면 하나씩 빼기)
        if (caridArray_history.size() >= 20) {
            caridArray_history.remove(caridArray_history.size()-1);
        }

        //검색기록에 저장 (그리고 검색기록 fragment 초기화 해야함.)
        caridArray_history.add(0, car_id);
        pref.setStringArrayPref(mycontext, "history", caridArray_history);
    }
}

