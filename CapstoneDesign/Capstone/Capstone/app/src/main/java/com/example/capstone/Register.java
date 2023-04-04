package com.example.capstone;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;


public class Register extends AppCompatActivity {

    private Button checkid;
    private Button registerbutton;


    private TextView id;
    private EditText tempid;
    private EditText pw;
    private EditText email;
    private EditText name;
    private EditText age;
    private RadioButton male;
    private RadioButton female;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        setTitle("가입하기");
        super.onCreate(saveInstanceState);
        setContentView(R.layout.register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_register);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        //UI랑 연결하기
        id = findViewById(R.id.register_id_noedit);
        tempid = findViewById(R.id.register_id);
        pw = findViewById(R.id.register_pw);
        email = findViewById(R.id.register_email);
        checkid = findViewById(R.id.id_check_button);
        registerbutton = findViewById(R.id.register_button);
        name = findViewById(R.id.register_name);
        age = findViewById(R.id.register_age);
        male = findViewById(R.id.register_male);
        female = findViewById(R.id.register_female);

        //주소
        String myurl = "http://" + getString(R.string.net_ip) + "/registration.php";

        //중복체크주소
        String checkmyurl = "http://" + getString(R.string.net_ip) + "/idvalidation.php";

        Log.d("테스트", myurl);

        //중복체크 버튼
        //True면 사용가능 False면 중복됨
        checkid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                if(status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                    // EditText값 예외처리
                    if(tempid.getText().toString().trim().length() > 0){

                        // get방식 파라미터 추가
                        HttpUrl.Builder urlBuilder = HttpUrl.parse(checkmyurl).newBuilder();
                        urlBuilder.addQueryParameter("v", "1.0"); // 예시
                        String url = urlBuilder.build().toString();

                        // userid를 전달
                        RequestBody formBody = new FormBody.Builder()
                                .add("userid", tempid.getText().toString().trim())
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
                                                Log.i("tag", "응답실패");
                                                Toast.makeText(getApplicationContext(), "네트워크 문제 발생", Toast.LENGTH_SHORT).show();

                                            } else {
                                                // 응답 성공
                                                Log.i("tag", "응답 성공");
                                                final String responseData = response.body().string();
                                                if(responseData.equals("True")) {
                                                    //중복 아이디 아님
                                                    //Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_SHORT).show();
                                                    Intent nextIntent = new Intent(getApplicationContext(), Idcheck_success.class);
                                                    nextIntent.putExtra("my_id",tempid.getText().toString());
                                                    startActivityForResult(nextIntent, 1);


                                                }else {
                                                    //중복 아이디 임
                                                    //Toast.makeText(getApplicationContext(), "중복된 아이디 임" + responseData, Toast.LENGTH_SHORT).show();
                                                    Intent nextIntent = new Intent(getApplicationContext(), Idcheck_fail.class);
                                                    nextIntent.putExtra("my_id",tempid.getText().toString());
                                                    startActivityForResult(nextIntent, 2);
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        });
                    }else{
                        Toast.makeText(getApplicationContext(), "아이디를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //가입하기 버튼
        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                if(status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                    String gender = "";
                    if (male.isChecked()){
                        gender = "남성";
                    }
                    else if (female.isChecked()){
                        gender = "여성";
                    }
                    else{
                        gender = "";
                    }

                    // EditText값 예외처리
                    if(id.getText().toString().trim().length() > 0 &&
                            pw.getText().toString().trim().length() > 0 &&
                            email.getText().toString().trim().length() > 0 &&
                            name.getText().toString().trim().length() > 0 &&
                            age.getText().toString().trim().length() > 0 &&
                            gender.trim().length() > 0){

                        // 프로그래스바 보이게 처리
                        //findViewById(R.id.cpb).setVisibility(View.VISIBLE);

                        // get방식 파라미터 추가
                        HttpUrl.Builder urlBuilder = HttpUrl.parse(myurl).newBuilder();
                        urlBuilder.addQueryParameter("v", "1.0"); // 예시
                        String url = urlBuilder.build().toString();

                        // POST 파라미터 추가
                        RequestBody formBody = new FormBody.Builder()
                                .add("userid", id.getText().toString().trim())
                                .add("userpw", pw.getText().toString().trim())
                                .add("email", email.getText().toString().trim())
                                .add("username", name.getText().toString().trim())
                                .add("age", age.getText().toString().trim())
                                .add("gender", gender)
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
                                                Log.i("tag", "응답실패");
                                                Toast.makeText(getApplicationContext(), "네트워크 문제 발생", Toast.LENGTH_SHORT).show();

                                            } else {
                                                // 응답 성공
                                                Log.i("tag", "응답 성공");
                                                final String responseData = response.body().string();
                                                Log.d("테스트", responseData);
                                                if(responseData.equals("")) {
                                                    //성공
                                                    Toast.makeText(getApplicationContext(), "가입을 축하드립니다.", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                    //startActivityflag(MainActivity.class);
                                                }else {
                                                    Toast.makeText(getApplicationContext(), "회원가입에 실패 했습니다." + responseData, Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            }
                        });
                    }else{
                        Toast.makeText(getApplicationContext(), "빈값을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //데이터 받기

                //기존 id 위치 값 제거
                tempid = findViewById(R.id.register_id);
                tempid.setText("");

                //id값 등록
                String result = data.getStringExtra("result");
                id = findViewById(R.id.register_id_noedit);
                id.setText(result);
                id.setVisibility(View.VISIBLE);
            }
        }
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                //기존 id 위치 값 제거
                tempid = findViewById(R.id.register_id);
                tempid.setText("");
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
}
