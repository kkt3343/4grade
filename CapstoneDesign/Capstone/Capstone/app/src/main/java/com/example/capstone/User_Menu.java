package com.example.capstone;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class User_Menu extends AppCompatActivity {

    String mytoken = "";

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        setTitle("메뉴");
        super.onCreate(saveInstanceState);
        setContentView(R.layout.menu);

        //툴바 22.03.10
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        //메뉴세팅
        menuset();
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

    //메뉴 구성 함수
    public void menuset()
    {
        //Preference에 아이디 불러오기
        String userid = PreferenceManager.getString(getApplicationContext() ,"userid");
        //Preference에 로그인 여부 불러오기
        Boolean islogin = PreferenceManager.getBoolean(getApplicationContext(), "islogin");

        /* 리스트뷰 부분 */
        ArrayList<String> box = new ArrayList<>();
        if (islogin == true){
            box.add("로그인 정보: " + userid);
            box.add("로그아웃");
        }
        else{
            box.add("로그인하기");
        }

        //만약에 리스트뷰에 무언가 추가하고 싶다면 밑의 주석처럼 추가하면 됩니다.
        //box.add("나의 토큰값 확인하기");

        //로그아웃 주소
        String myurl = "http://" + getString(R.string.net_ip) + "/logout.php";

        ListView list = findViewById(R.id.menu_listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, box);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //위에 리스트뷰에 추가했다면 여기에도 추가해야합니다.
                if(box.get(position).equals("----")){
                }
                else{
                }

                if(box.get(position).equals("로그인하기")) {
                    Intent nextIntent = new Intent(getApplicationContext(), Login.class);
                    nextIntent.putExtra("test", box.get(position));
                    startActivity(nextIntent);
                }
                if(box.get(position).equals("로그아웃")) {
                    Log.d("주소",myurl);
                    //Preference에 아이디 정보 지우기
                    PreferenceManager.setString(getApplicationContext(), "userid", "");
                    //Preference에 로그인 여부 지우기
                    PreferenceManager.setBoolean(getApplicationContext(), "islogin", false);

                    int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                    if(status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {
                        // get방식 파라미터 추가
                        HttpUrl.Builder urlBuilder = HttpUrl.parse(myurl).newBuilder();
                        urlBuilder.addQueryParameter("v", "1.0"); // 예시
                        String url = urlBuilder.build().toString();

                        // POST 파라미터 추가
                        RequestBody formBody = new FormBody.Builder()
                                .add("userid", userid.trim())
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
                                                    //성공
                                                    Toast.makeText(getApplicationContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                                                    box.remove(1);
                                                    finish();
                                                    overridePendingTransition(0, 0);
                                                    startActivity(getIntent());
                                                    overridePendingTransition(0, 0);
                                                }else {
                                                    Toast.makeText(getApplicationContext(), "로그아웃 실패.", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            }
                        });
                    }else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    void show()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("서버와 연결 할 수 없음");
        builder.setMessage("서버와 연결할 수 없습니다.\n 앱을 종료합니다.");
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

    @Override
    protected void onResume() {
        super.onResume();
        //메뉴세팅
        menuset();
    }

    protected void showtoken() {
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

                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("label", "wer");
                        clipboard.setPrimaryClip(clip);
                    }
                });
    }
}