package com.example.capstone;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

//와이파이나 셀룰러 데이터 확인
class NetworkStatus {
    public static final int TYPE_WIFI = 1;
    public static final int TYPE_MOBILE = 2;
    public static final int TYPE_NOT_CONNECTED = 3;

    public static int getConnectivityStatus(Context context){ //해당 context의 서비스를 사용하기위해서 context객체를 받는다.
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if(networkInfo != null){
            int type = networkInfo.getType();
            if(type == ConnectivityManager.TYPE_MOBILE){//쓰리지나 LTE로 연결된것(모바일을 뜻한다.)
                return TYPE_MOBILE;
            }else if(type == ConnectivityManager.TYPE_WIFI){//와이파이 연결된것
                return TYPE_WIFI;
            }
        }
        return TYPE_NOT_CONNECTED;  //연결이 되지않은 상태
    }
}

//SQL 서버와 연결 확인
//참조 : https://mommoo.tistory.com/52
class CheckConnect extends Thread{
    private boolean success;
    private String host;

    private int whatnum = 0;
    public CheckConnect(String host){
        this.host = host;
    }

    @Override
    public void run() {

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection)new URL(host).openConnection();
            //conn.setRequestProperty("User-Agent","Android");
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();
            int responseCode = conn.getResponseCode();
            whatnum = responseCode;
            //http 코드 확인 : https://ooz.co.kr/260
            if(responseCode == 200 || responseCode == 204 || responseCode == 301 || responseCode == 302 || responseCode == 303) {
                success = true;
            }
            else {
                success = false;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        if(conn != null){
            conn.disconnect();
        }
    }
    public boolean isSuccess(){
        return success;
    }
    public int getint() {return whatnum;}
}
//비트맵통신
class network_bitmap extends Thread{
    public String site = "";
    Bitmap bitmap;
    public void run(){
        try {
            URL url = new URL(site);
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
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Bitmap bit(){
        return bitmap;
    }
}

//JSON통신
class networkthread extends Thread{
    private ArrayList<String> text = new ArrayList<>();
    public String site = "";
    public String keyword = "";
    private String texttmp = "";
    public synchronized void run(){
        try {
            URL url = new URL(this.site);

            HttpURLConnection conn = null;
            conn = (HttpURLConnection) url.openConnection();

            //접속오류시 연결안함
            conn.setConnectTimeout(10000);
            conn.connect();

            InputStream input = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(input);

            BufferedReader br = new BufferedReader(isr);

            String str = "";
            StringBuffer buf = new StringBuffer();
            do{
                str = br.readLine();
                if(str!=null){
                    buf.append(str);
                }
            }while(str!=null);

            JSONObject root = new JSONObject(buf.toString());

            JSONArray result = root.getJSONArray("result");
            int lenOfresult = result.length();
            for (int i=0;i<lenOfresult;i++){

                JSONObject obj2 = result.getJSONObject(i);
                texttmp = obj2.getString(keyword);
                text.add(texttmp);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<String> text(){
        return this.text;
    }
}

//JSON통신_notice
class networkthread_notice extends Thread{
    //model
    private ArrayList<String> text = new ArrayList<>();
    public String site = "";
    public String keyword = "";

    //manufacturer
    private ArrayList<String> text2 = new ArrayList<>();
    public String keyword2 = "";

    public synchronized void run(){
        try {
            URL url = new URL(this.site);

            HttpURLConnection conn = null;
            conn = (HttpURLConnection) url.openConnection();

            //접속오류시 연결안함
            conn.setConnectTimeout(10000);
            conn.connect();

            InputStream input = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(input);

            BufferedReader br = new BufferedReader(isr);

            String str = "";
            StringBuffer buf = new StringBuffer();
            do{
                str = br.readLine();
                if(str!=null){
                    buf.append(str);
                }
            }while(str!=null);

            JSONObject root = new JSONObject(buf.toString());

            JSONArray result = root.getJSONArray("result");
            int lenOfresult = result.length();

            //Log.d("테스트3", String.valueOf(lenOfresult));

            for (int i=0;i<lenOfresult;i++){

                JSONObject obj2 = result.getJSONObject(i);
                text.add(obj2.getString(keyword));
                text2.add(obj2.getString(keyword2));

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<String> text(){
        return this.text;
    }
    public ArrayList<String> text2(){
        return this.text2;
    }
}