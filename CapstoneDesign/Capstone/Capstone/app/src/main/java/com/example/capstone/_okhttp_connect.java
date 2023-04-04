package com.example.capstone;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//조회수 증가 전달하기 22.04.09
public class _okhttp_connect {
    public void update_views(String carid, String myurl){
        // get방식 파라미터 추가
        HttpUrl.Builder urlBuilder = HttpUrl.parse(myurl).newBuilder();
        urlBuilder.addQueryParameter("v", "1.0"); // 예시
        String url = urlBuilder.build().toString();

        // POST 파라미터 추가
        RequestBody formBody = new FormBody.Builder()
                .add("carid", carid)
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
                try {
                    if (!response.isSuccessful()) {
                        // 응답 실패
                        Log.i("tag", "응답실패");
                    } else {
                        // 응답 성공
                        Log.i("tag", "응답 성공");
                        final String responseData = response.body().string();
                        if(responseData.equals("")) {
                            //성공
                        }
                        else {
                            //실패
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

class _okhttp_connect_get extends Thread{
    private ArrayList<ArrayList> sales = new ArrayList<>();
    public void makesales(){
        ArrayList<String> car_id = new ArrayList<>();
        ArrayList<String> url = new ArrayList<>();
        ArrayList<String> site = new ArrayList<>();
        ArrayList<String> title = new ArrayList<>();
        ArrayList<String> carnumber = new ArrayList<>();
        ArrayList<String> cartype = new ArrayList<>();
        ArrayList<String> manufacturer = new ArrayList<>();
        ArrayList<String> model = new ArrayList<>();
        ArrayList<String> model_detail = new ArrayList<>();
        ArrayList<String> price = new ArrayList<>();
        ArrayList<String> distance = new ArrayList<>();
        ArrayList<String> displacement = new ArrayList<>();
        ArrayList<String> caryear = new ArrayList<>();
        ArrayList<String> carcolor = new ArrayList<>();
        ArrayList<String> carfuel = new ArrayList<>();
        ArrayList<String> imglink = new ArrayList<>();
        sales.add(car_id);
        sales.add(url);
        sales.add(site);
        sales.add(title);
        sales.add(carnumber);
        sales.add(cartype);
        sales.add(manufacturer);
        sales.add(model);
        sales.add(model_detail);
        sales.add(price);
        sales.add(distance);
        sales.add(displacement);
        sales.add(caryear);
        sales.add(carcolor);
        sales.add(carfuel);
        sales.add(imglink);
    }

    private ArrayList<String> car_id = new ArrayList<>();
    public String site = "";
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
                sales.get(0).add(obj2.get("carid"));
                sales.get(1).add(obj2.get("url"));
                sales.get(2).add(obj2.get("site"));
                sales.get(3).add(obj2.get("title"));
                sales.get(4).add(obj2.get("carnumber"));
                sales.get(5).add(obj2.get("cartype"));
                sales.get(6).add(obj2.get("manufacturer"));
                sales.get(7).add(obj2.get("model"));
                sales.get(8).add(obj2.get("model_detail"));
                sales.get(9).add(obj2.get("price"));
                sales.get(10).add(obj2.get("distance"));
                sales.get(11).add(obj2.get("displacement"));
                sales.get(12).add(obj2.get("caryear"));
                sales.get(13).add(obj2.get("carcolor"));
                sales.get(14).add(obj2.get("carfuel"));
                sales.get(15).add(obj2.get("imglink"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<ArrayList> get_sales(){
        return this.sales;
    }
}