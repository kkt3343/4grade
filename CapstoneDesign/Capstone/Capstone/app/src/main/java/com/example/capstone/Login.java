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
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login extends AppCompatActivity {

    private Button login_button;
    private Button register_button;
    private EditText login_id;
    private EditText login_pw;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        setTitle("로그인");
        super.onCreate(saveInstanceState);
        setContentView(R.layout.login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        login_button = findViewById(R.id.login_button);
        register_button = findViewById(R.id.login_register);

        login_id = findViewById(R.id.login_id);
        login_pw = findViewById(R.id.login_pw);

        //로그인주소
        String myurl = "http://" + getString(R.string.net_ip) + "/login.php";

        //토큰 정보
        String mytoken;
        mytoken = PreferenceManager.getString(getApplicationContext() ,"token");
        //Toast.makeText(getApplicationContext(),mytoken,Toast.LENGTH_SHORT).show();

        //로그인하기
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                if(status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {
                    
                    // 아이디와 비밀번호 입력 받기
                    if(login_id.getText().toString().trim().length() > 0 &&
                            login_pw.getText().toString().trim().length() > 0 ){

                        // 프로그래스바 보이게 처리
                        //findViewById(R.id.cpb).setVisibility(View.VISIBLE);

                        // get방식 파라미터 추가
                        HttpUrl.Builder urlBuilder = HttpUrl.parse(myurl).newBuilder();
                        urlBuilder.addQueryParameter("v", "1.0"); // 예시
                        String url = urlBuilder.build().toString();

                        // POST 파라미터 추가
                        RequestBody formBody = new FormBody.Builder()
                                .add("userid", login_id.getText().toString().trim())
                                .add("userpw", login_pw.getText().toString().trim())
                                .add("token", mytoken)
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
                                                if(responseData.equals("1")) {
                                                    //성공
                                                    Toast.makeText(getApplicationContext(), login_id.getText().toString() + " 고객님 환영합니다.", Toast.LENGTH_SHORT).show();

                                                    //Preference에 아이디 정보 저장
                                                    PreferenceManager.setString(getApplicationContext(), "userid", login_id.getText().toString());
                                                    //Preference에 로그인 여부 저장
                                                    PreferenceManager.setBoolean(getApplicationContext(), "islogin", true);
                                                    finish();
                                                    //startActivityflag(MainActivity.class);
                                                }
                                                else if(responseData.equals("2")){
                                                    //중복 로그인
                                                    Toast.makeText(getApplicationContext(), login_id.getText().toString() + " 고객님 환영합니다." + "\n기존 로그인된 기기에서 로그아웃 됩니다.", Toast.LENGTH_SHORT).show();

                                                    //Preference에 아이디 정보 저장
                                                    PreferenceManager.setString(getApplicationContext(), "userid", login_id.getText().toString());
                                                    //Preference에 로그인 여부 저장
                                                    PreferenceManager.setBoolean(getApplicationContext(), "islogin", true);
                                                    finish();
                                                }
                                                else {
                                                    Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호가 틀립니다.", Toast.LENGTH_SHORT).show();
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

        //가입하기
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(getApplicationContext(), Register.class);
                startActivity(nextIntent);
            }
        });

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
