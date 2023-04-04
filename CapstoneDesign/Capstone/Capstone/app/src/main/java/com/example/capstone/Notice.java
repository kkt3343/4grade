package com.example.capstone;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Notice extends AppCompatActivity {

    //스피너 데이터를 저장할 ArrayList
    ArrayList<String> notice_model1_manufacturer = new ArrayList<>(); //국산
    ArrayList<String> notice_model2_manufacturer = new ArrayList<>(); //수입

    ArrayList<String> notice_model_model = new ArrayList<>(); //국산 & 수입 모델명 받아오는 곳


    ArrayList<String> notice_caryear = new ArrayList<>();
    ArrayList<String> notice_distance = new ArrayList<>();
    ArrayList<String> notice_price = new ArrayList<>();

    //알람데이터 저장할 변수
    ArrayList<String> box = new ArrayList<String>();
    LinearLayout notice_linear;
    
    //동적배열
    ArrayList<TextView> dynamic_text = new ArrayList();
    ArrayList<LinearLayout> dynamic_layout = new ArrayList();
    ArrayList<Button> dynamic_button = new ArrayList();

    ArrayList<String> noti_arr = new ArrayList<>();

    Button addbtn;

    Spinner[] setspinner = new Spinner[10];

    int[] setspinnerid = {R.id.notice_manufacturer_spinner, R.id.notice_model_name_spinner,
            R.id.caryear_from, R.id.caryear_to,
            R.id.distance_from, R.id.distance_to,
            R.id.price_from,R.id.price_to};

    ArrayAdapter<String> setspinneradapter[] = new ArrayAdapter[10];


    String[] caryear = {"없음", "1980 이전",
            "1980", "1981", "1982","1983", "1984",
            "1985", "1986", "1987", "1988", "1989",
            "1990", "1991", "1992", "1993", "1994",
            "1995", "1996", "1997", "1998", "1999",
            "2000", "2001", "2002", "2003", "2004",
            "2005", "2006", "2007", "2008", "2009",
            "2010", "2011", "2012", "2013", "2014",
            "2015", "2016", "2017", "2018", "2019",
            "2020", "2021", "2022"};

    //단위 KM
    String[] distance = {"없음", "1000 미만", "1000", "2000", "3000", "4000", "5000", "6000", "7000", "8000", "9000",
            "10000", "20000", "30000", "40000", "50000", "60000", "70000", "80000", "90000",
            "100000", "150000", "200000", "250000", "300000", "400000", "400000 초과"};

    String[] price = {"없음", "500만 미만", "1000만 미만", "1000만", "2000만", "3000만", "4000만", "5000만", "6000만", "7000만", "8000만", "9000만",
            "1억", "2억", "3억", "4억", "5억", "5억 초과"};

    //로그인 여부 체크
    Boolean islogin = false;
    String notify_insert;
    ArrayList<String> notice_arr = new ArrayList<>();
    String userid;

    //알림등록개수
    int lenOfresult;

    //Spinner 변환을 위한 코드
    String caryear_f, caryear_t, distance_f, distance_t, price_f, price_t;

    ArrayList<String> noti_num = new ArrayList<>();
    ArrayList<String> caryear_from = new ArrayList<>();
    ArrayList<String> caryear_to = new ArrayList<>();
    ArrayList<String> distance_from = new ArrayList<>();
    ArrayList<String> distance_to = new ArrayList<>();
    ArrayList<String> price_from = new ArrayList<>();
    ArrayList<String> price_to = new ArrayList<>();

    ArrayList<String> manufacturer_name = new ArrayList<>();
    ArrayList<String> model_name = new ArrayList<>();

    Boolean isfirst = true;
    Boolean isdomestice = true;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        setTitle("알림설정");
        super.onCreate(saveInstanceState);
        setContentView(R.layout.notice);

        //툴바 22.03.10
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_notice);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        //스피너 부분은 여기서 처리한다.
        //검색형 스피너설정
        SearchableSpinner searchspinner1 = findViewById(R.id.notice_manufacturer_spinner);
        searchspinner1.setTitle("원하는 제조사를 선택하세요.");
        searchspinner1.setPositiveButton("완료");

        SearchableSpinner searchspinner2 = findViewById(R.id.notice_model_name_spinner);
        searchspinner2.setTitle("원하는 차량을 선택하세요.");
        searchspinner2.setPositiveButton("완료");

        //로그인 여부 확인
        notify_insert = "http://"+getString(R.string.net_ip)+"/notify_insert.php";
        //Preference에 로그인 여부 확인
        islogin = PreferenceManager.getBoolean(getApplicationContext(), "islogin");
        userid = PreferenceManager.getString(getApplicationContext(), "userid");

        if (islogin == false) {
            //로그인이 되어 있지 않으면 종료
            show();
        }
        else{
            //로그인이 되어있다면 값을 받아온다.
            //값을 받아오기 / 국산
            networkthread net1 = new networkthread();
            //국산
            net1.site = "http://"+getString(R.string.net_ip)+"/car_manufacturer_d.php";
            net1.keyword = "manufacturer";
            net1.start();
            try {
                net1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            notice_model1_manufacturer = net1.text();

            //값을 받아오기 / 수입
            networkthread net2 = new networkthread();
            //국산
            net2.site = "http://"+getString(R.string.net_ip)+"/car_manufacturer_i.php";
            net2.keyword = "manufacturer";
            net2.start();
            try {
                net2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            notice_model2_manufacturer = net2.text();
        }

        //스피너값 설정
        for(int i=0;i<caryear.length;i++){
            notice_caryear.add(caryear[i]);
        }
        for(int i=0;i<distance.length;i++){
            notice_distance.add(distance[i]);
        }
        for(int i=0;i<price.length;i++){
            notice_price.add(price[i]);
        }

        //국산 수입
        Button notice_domestice = findViewById(R.id.notice_domestic);
        Button notice_overseas = findViewById(R.id.notice_overseas);

        notice_domestice.setBackgroundResource(R.drawable.btn_domestic_click);
        TextView notice_manufacturer_name = findViewById(R.id.notice_manufacturer_name);
        TextView notice_model_name = findViewById(R.id.notice_model_name);
        notice_manufacturer_name.setText("국산 제조사");
        notice_model_name.setText("모델명");


        //스피너 설정
        for(int i=0; i<setspinnerid.length; i++){
            setspinner[i] = (Spinner)findViewById(setspinnerid[i]);
        }

        notice_domestice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notice_domestice.setBackgroundResource(R.drawable.btn_domestic_click);
                notice_overseas.setBackgroundResource(R.drawable.btn_overseas);
                notice_manufacturer_name.setText("국산 제조사");
                notice_model_name.setText("모델명");

                //제조사
                setspinneradapter[0] = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, notice_model1_manufacturer);
                setspinneradapter[0].setDropDownViewResource(android.R.layout.simple_list_item_1);
                setspinner[0].setAdapter(setspinneradapter[0]);

                isdomestice = true;
            }
        });
        notice_overseas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notice_domestice.setBackgroundResource(R.drawable.btn_domestic);
                notice_overseas.setBackgroundResource(R.drawable.btn_overseas_click);
                notice_manufacturer_name.setText("수입 제조사");
                notice_model_name.setText("모델명");

                //제조사
                setspinneradapter[0] = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, notice_model2_manufacturer);
                setspinneradapter[0].setDropDownViewResource(android.R.layout.simple_list_item_1);
                setspinner[0].setAdapter(setspinneradapter[0]);

                isdomestice = false;
            }
        });

        if(isfirst){
            //제조사
            setspinneradapter[0] = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, notice_model1_manufacturer);
            setspinneradapter[0].setDropDownViewResource(android.R.layout.simple_list_item_1);
            setspinner[0].setAdapter(setspinneradapter[0]);
            isfirst = false;
            isdomestice = true;
            //모델명
            setspinneradapter[1] = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, notice_model_model);
            setspinneradapter[1].setDropDownViewResource(android.R.layout.simple_list_item_1);
            setspinner[1].setAdapter(setspinneradapter[1]);
        }

        //만약 제조사 클릭시, 정보를 받아옴
        setspinner[0].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0){ // 없음
                    //선택된 값으로 다시 한번 통신하기
                    networkthread net2 = new networkthread();
                    if(isdomestice){
                        net2.site = "http://"+getString(R.string.net_ip)+"/car_model_d.php?manufacturer="+setspinner[0].getSelectedItem().toString();
                    }else{
                        net2.site = "http://"+getString(R.string.net_ip)+"/car_model_i.php?manufacturer="+setspinner[0].getSelectedItem().toString();
                    }
                    net2.keyword = "model";
                    net2.start();

                    try {
                        net2.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    notice_model_model = net2.text();

                    setspinneradapter[1].clear();

                    setspinneradapter[1] = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, notice_model_model);
                    setspinneradapter[1].setDropDownViewResource(android.R.layout.simple_list_item_1);
                    setspinner[1].setAdapter(setspinneradapter[1]);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        setspinneradapter[2] = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notice_caryear);
        setspinneradapter[3] = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notice_caryear);
        setspinneradapter[4] = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notice_distance);
        setspinneradapter[5] = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notice_distance);
        setspinneradapter[6] = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notice_price);
        setspinneradapter[7] = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notice_price);
        for(int i=2; i<setspinnerid.length; i++){
            setspinneradapter[i].setDropDownViewResource(android.R.layout.simple_list_item_1);
            setspinner[i].setAdapter(setspinneradapter[i]);
        }

        setspinner[3].setEnabled(false);
        setspinner[5].setEnabled(false);
        setspinner[7].setEnabled(false);

        //스피너 설정
        //연식
        setspinner[2].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){ // 없음
                    setspinner[3].setEnabled(false);
                    setspinneradapter[3] = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, notice_caryear);
                    setspinneradapter[3].setDropDownViewResource(android.R.layout.simple_list_item_1);
                    setspinner[3].setAdapter(setspinneradapter[3]);
                }
                else if(position == 1){ //1980이전
                    setspinner[3].setEnabled(false);
                    setspinneradapter[3] = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, notice_caryear);
                    setspinneradapter[3].setDropDownViewResource(android.R.layout.simple_list_item_1);
                    setspinner[3].setAdapter(setspinneradapter[3]);
                }
                else{ //그외
                    setspinner[3].setEnabled(true);
                    setspinneradapter[3] = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, notice_caryear.subList(position, notice_caryear.size()));
                    setspinneradapter[3].setDropDownViewResource(android.R.layout.simple_list_item_1);
                    setspinner[3].setAdapter(setspinneradapter[3]);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //단위 KM
        setspinner[4].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){ //없음
                    setspinner[5].setEnabled(false);
                    setspinneradapter[5] = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, notice_distance);
                    setspinneradapter[5].setDropDownViewResource(android.R.layout.simple_list_item_1);
                    setspinner[5].setAdapter(setspinneradapter[5]);
                }
                else if (position == 1){ //1000미만
                    setspinner[5].setEnabled(false);
                    setspinneradapter[5] = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, notice_distance);
                    setspinneradapter[5].setDropDownViewResource(android.R.layout.simple_list_item_1);
                    setspinner[5].setAdapter(setspinneradapter[5]);
                }
                else if (position == distance.length - 1){ //40만 초과
                    setspinner[5].setEnabled(false);
                    setspinneradapter[5] = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, notice_distance);
                    setspinneradapter[5].setDropDownViewResource(android.R.layout.simple_list_item_1);
                    setspinner[5].setAdapter(setspinneradapter[5]);
                }
                else{
                    setspinner[5].setEnabled(true);
                    setspinneradapter[5] = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, notice_distance.subList(position, notice_distance.size()-1));
                    setspinneradapter[5].setDropDownViewResource(android.R.layout.simple_list_item_1);
                    setspinner[5].setAdapter(setspinneradapter[5]);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //가격
        setspinner[6].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){ //없음
                    setspinner[7].setEnabled(false);
                    setspinneradapter[7] = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, notice_price);
                    setspinneradapter[7].setDropDownViewResource(android.R.layout.simple_list_item_1);
                    setspinner[7].setAdapter(setspinneradapter[7]);
                }
                else if(position == 1){ //500만 미만
                    setspinner[7].setEnabled(false);
                    setspinneradapter[7] = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, notice_price);
                    setspinneradapter[7].setDropDownViewResource(android.R.layout.simple_list_item_1);
                    setspinner[7].setAdapter(setspinneradapter[7]);
                }
                else if(position == 2){ //1000만 미만
                    setspinner[7].setEnabled(false);
                    setspinneradapter[7] = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, notice_price);
                    setspinneradapter[7].setDropDownViewResource(android.R.layout.simple_list_item_1);
                    setspinner[7].setAdapter(setspinneradapter[7]);
                }
                else if(position == price.length - 1) { //5억 초과
                    setspinner[7].setEnabled(false);
                    setspinneradapter[7] = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, notice_price);
                    setspinneradapter[7].setDropDownViewResource(android.R.layout.simple_list_item_1);
                    setspinner[7].setAdapter(setspinneradapter[7]);
                }
                else{
                    setspinner[7].setEnabled(true);
                    setspinneradapter[7] = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, notice_price.subList(position, notice_price.size()-1));
                    setspinneradapter[7].setDropDownViewResource(android.R.layout.simple_list_item_1);
                    setspinner[7].setAdapter(setspinneradapter[7]);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //스크롤뷰 리니어 레이아웃 설정
        notice_linear = findViewById(R.id.notice_linear);
        addbtn = findViewById(R.id.addnotice);
        //추가하기 버튼 클릭시

        set_notice s = new set_notice();
        add_notice a = new add_notice();

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                if(status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                    if(lenOfresult < 10){
                        a.run();
                        //알림 설정
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "내역이 많아, 더 이상 추가 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        try {
            s.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
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

    void show()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("로그인이 되어 있지 않음");
        builder.setMessage("로그인이 되어 있지 않아 알림 기능을 사용할 수 없습니다.\n 로그인을 해주세요.");
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
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

    //이 클래스는 알림 추가 클래스
    class add_notice {
        public void run(){
            //전달해줄 숫자 변환
            if (setspinner[2].getSelectedItem().toString().equals("1980 이전")){
                caryear_f = "NULL";
                caryear_t = "1979";
            }
            else
            {
                caryear_f = setspinner[2].getSelectedItem().toString().replace("없음","NULL");
                caryear_t = setspinner[3].getSelectedItem().toString().replace("없음","NULL");
            }

            //KM
            if (setspinner[4].getSelectedItem().toString().equals("1000 미만"))
            {
                distance_f = "NULL";
                distance_t = "1000";
            }
            else if (setspinner[5].getSelectedItem().toString().equals("400000 초과"))
            {
                distance_f = "400001";
                distance_t = "NULL";
            }
            else
            {
                distance_f = setspinner[4].getSelectedItem().toString().replace("없음","NULL");
                distance_t = setspinner[5].getSelectedItem().toString().replace("없음","NULL");
            }

            //가격
            if (setspinner[6].getSelectedItem().toString().equals("500만 미만"))
            {
                price_f = "NULL";
                price_t = "5000000";
            }
            else if(setspinner[6].getSelectedItem().toString().equals("1000만 미만"))
            {
                price_f = "NULL";
                price_t = "10000000";
            }
            else if(setspinner[6].getSelectedItem().toString().equals("5억 초과"))
            {
                price_f = "500000001";
                price_t = "NULL";
            }
            else
            {
                price_f = setspinner[6].getSelectedItem().toString().replace("만","0000").replace("억","00000000").replace("없음","NULL");
                price_t = setspinner[7].getSelectedItem().toString().replace("만","0000").replace("억","00000000").replace("없음","NULL");
            }

            // get방식 파라미터 추가
            HttpUrl.Builder urlBuilder = HttpUrl.parse(notify_insert).newBuilder();
            urlBuilder.addQueryParameter("v", "1.0"); // 예시
            String url = urlBuilder.build().toString();

            // POST 파라미터 추가
            RequestBody formBody = new FormBody.Builder()
                    .add("userid", userid)
                    .add("manufacturer", setspinner[0].getSelectedItem().toString())
                    .add("model", setspinner[1].getSelectedItem().toString()) //22.04.03
                    .add("caryear_from", caryear_f)
                    .add("caryear_to", caryear_t)
                    .add("distance_from", distance_f)
                    .add("distance_to", distance_t)
                    .add("price_from", price_f)
                    .add("price_to", price_t)
                    .build();

            // 요청 만들기
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();

            // 응답 콜백
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    // 서브 스레드 Ui 변경 할 경우 에러
                    // 메인스레드 Ui 설정
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // 프로그래스바 안보이게 처리
                                //findViewById(R.id.cpb).setVisibility(View.GONE);
                                if (!response.isSuccessful()) {
                                    // 응답 실패
                                    Toast.makeText(getApplicationContext(), "네트워크 문제 발생", Toast.LENGTH_SHORT).show();

                                } else {
                                    // 응답 성공
                                    final String responseData = response.body().string();
                                    Toast.makeText(getApplicationContext(),responseData,Toast.LENGTH_SHORT).show();
                                    if(responseData.equals("")) { } else { }

                                    //noti num 추가
                                    noti_arr.add(responseData);
                                }
                            } catch (Exception e) {
                                Log.d("tag2", "응답실패");
                                e.printStackTrace();
                            }
                        }
                    });

                }
            });
        }
    }

    //이 클래스는 알림 받아오기 클래스
    class set_notice{
        public void run() throws InterruptedException {

            String myurl = "http://" + getString(R.string.net_ip) + "/notify_load.php";

            // get방식 파라미터 추가
            HttpUrl.Builder urlBuilder = HttpUrl.parse(myurl).newBuilder();
            urlBuilder.addQueryParameter("v", "1.0"); // 예시
            String url = urlBuilder.build().toString();

            // POST 파라미터 추가
            RequestBody formBody = new FormBody.Builder()
                    .add("userid", userid)
                    .build();

            // 요청 만들기
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();

            CountDownLatch countDownLatch = new CountDownLatch(1);

            // 응답 콜백
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
                @Override
                public void onResponse(Call call, final Response response) throws IOException {

                    // 서브 스레드 Ui 변경 할 경우 에러
                    // 메인스레드 Ui 설정
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (!response.isSuccessful()) {
                                    // 응답 실패
                                    Log.i("tag", "응답실패");
                                    Toast.makeText(getApplicationContext(), "네트워크 문제 발생", Toast.LENGTH_SHORT).show();

                                } else {
                                    // 응답 성공
                                    Log.i("tag", "응답 성공");
                                    final String responseData = response.body().string();
                                    Log.d("테스트", responseData);

                                    //JSON데이터 가져와서 처리하기
                                    JSONObject root = new JSONObject(responseData);

                                    JSONArray result = root.getJSONArray("result");

                                    lenOfresult = result.length();

                                    for (int i=0; i < lenOfresult;i++){

                                        JSONObject obj2 = result.getJSONObject(i);

                                        noti_num.add(obj2.getString("noti_num"));
                                        caryear_from.add(obj2.getString("caryear_from"));
                                        caryear_to.add(obj2.getString("caryear_to"));
                                        distance_from.add(obj2.getString("distance_from"));
                                        distance_to.add(obj2.getString("distance_to"));
                                        String pf = obj2.getString("price_from");
                                        String pt = obj2.getString("price_to");

                                        //제조사 추가
                                        manufacturer_name.add(obj2.getString("manufacturer"));
                                        model_name.add(obj2.getString("model"));

                                        price_from.add(pf.replace("00000000","억").replace("0000000","000만").replace("000000","00만"));
                                        price_to.add(pt.replace("00000000","억").replace("0000000","000만").replace("000000","00만"));

                                    }

                                    //어떻게 기다릴지 정하기.
                                    set_notice_linear();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        }
    }

    private void set_notice_linear(){
        //설정내역 보여주기 (만약 NULL 값이면 설정 안함으로 보여 주는 코드)
        String tmp1, tmp2, tmp3;

        for (int i=0; i < noti_num.size() ;i++) {
            //연식
            if (caryear_from.get(i).equals("") && caryear_to.get(i).equals("")) {
                tmp1 = "연식: 설정 안함\n";
            } else if (caryear_from.get(i).equals("") && !caryear_to.get(i).equals("")) {
                if (caryear_to.get(i).equals("1979")) {
                    caryear_to.set(i, "1980");
                }
                tmp1 = "연식: " + caryear_to.get(i) + "년이전\n";
            } else {
                tmp1 = "연식: " + caryear_from.get(i) + "년부터 ~ " + caryear_to.get(i) + "년까지\n";
            }

            //KM수
            if (distance_from.get(i).equals("") && distance_to.get(i).equals("")) {
                tmp2 = "KM수: 설정 안함\n";
            } else if (distance_from.get(i).equals("") && !distance_to.get(i).equals("")) {
                tmp2 = "KM수: " + distance_to.get(i) + "KM미만\n";
            } else if (!distance_from.get(i).equals("") && distance_to.get(i).equals("")) {
                if (distance_from.get(i).equals("400001")) {
                    distance_from.set(i, "400000");
                }
                tmp2 = "KM수: " + distance_from.get(i) + "KM초과(40만 포함X)\n";
            } else {
                tmp2 = "KM수: " + distance_from.get(i) + "KM부터 ~ " + distance_to.get(i) + "KM까지\n";
            }

            //가격
            if (price_from.get(i).equals("") && price_to.get(i).equals("")) {
                tmp3 = "가격: 설정 안함\n";
            } else if (price_from.get(i).equals("") && !price_to.get(i).equals("")) {
                tmp3 = "가격: " + price_to.get(i) + "원미만\n";
            } else if (!price_from.get(i).equals("") && price_to.get(i).equals("")) {
                if (price_from.get(i).equals("5000만1")) {
                    price_from.set(i, "5억");
                }
                tmp3 = "가격: " + price_from.get(i) + "원초과(5억 포함X)\n";
            } else {
                tmp3 = "가격: " + price_from.get(i) + "원이상 ~ " + price_to.get(i) + "원이하";
            }

            String noti_txt = "차량명: " + manufacturer_name.get(i) + " / " + model_name.get(i) + "\n" + tmp1 + tmp2 + tmp3;

            noti_arr.add(noti_num.get(i));

            //레이아웃을 추가
            dynamic_layout.add(new LinearLayout(getApplicationContext()));

            //글씨 레이어 4 : 버튼 레이어 1
            dynamic_layout.get(i).setWeightSum(5f);
            dynamic_layout.get(i).setOrientation(LinearLayout.HORIZONTAL);

            //데이터 추가
            dynamic_text.add(new TextView(getApplicationContext()));
            LinearLayout.LayoutParams tmptxt = new LinearLayout.LayoutParams(
                    0, ActionBar.LayoutParams.WRAP_CONTENT);
            tmptxt.weight = 4f;
            tmptxt.gravity = Gravity.CENTER;
            //알림정보 텍스트
            dynamic_text.get(i).setText(noti_txt);
            dynamic_layout.get(i).addView(dynamic_text.get(i), tmptxt);

            //삭제버튼 추가
            dynamic_button.add(new Button((getApplicationContext())));
            LinearLayout.LayoutParams tmpbtn = new LinearLayout.LayoutParams(
                    0, ActionBar.LayoutParams.WRAP_CONTENT);
            tmpbtn.weight = 1f;
            tmpbtn.gravity = Gravity.CENTER;
            dynamic_button.get(i).setBackground(getDrawable(R.drawable.btn_normal));
            dynamic_button.get(i).setText("삭제");
            dynamic_layout.get(i).addView(dynamic_button.get(i), tmpbtn);

            //Border라인 만들기
            dynamic_layout.get(i).setBackgroundResource(R.drawable.border_bottom);

            notice_linear.addView(dynamic_layout.get(i));

            //삭제버튼
            int finalI = i;
            String del_noti_num = noti_arr.get(i);
            dynamic_button.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    lenOfresult--;

                    notice_linear.removeView(dynamic_layout.get(finalI));
                    noti_arr.remove(del_noti_num);

                    //배열에서 제거
                    //서버와 통신해서 해당 알림 제거
                    // get방식 파라미터 추가
                    String del_url = "http://" + getString(R.string.net_ip) + "/notify_delete.php";

                    HttpUrl.Builder urlBuilder = HttpUrl.parse(del_url).newBuilder();
                    urlBuilder.addQueryParameter("v", "1.0"); // 예시
                    String url = urlBuilder.build().toString();

                    // POST 파라미터 추가
                    RequestBody formBody = new FormBody.Builder()
                            .add("noti_num", del_noti_num)
                            .build();

                    // 요청 만들기
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .post(formBody)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, final Response response) throws IOException {
                            // 서브 스레드 Ui 변경 할 경우 에러
                            // 메인스레드 Ui 설정
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        // 프로그래스바 안보이게 처리
                                        //findViewById(R.id.cpb).setVisibility(View.GONE);

                                        if (!response.isSuccessful()) {
                                            // 응답 실패
                                            Log.i("tag", "응답실패");
                                            Toast.makeText(getApplicationContext(), "네트워크 문제 발생", Toast.LENGTH_SHORT).show();

                                        } else {
                                            // 응답 성공
                                            Log.i("tag", "응답 성공");
                                            final String responseData = response.body().string();
                                            if (responseData.equals("")) {
                                            } else {
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    });
                }
            });
        }
    }
}

