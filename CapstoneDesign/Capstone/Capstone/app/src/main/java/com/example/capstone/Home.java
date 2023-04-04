package com.example.capstone;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Locale;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

public class Home extends Fragment {
    //버튼
    private Button main_searchbutton;

    //키워드로 검색하기 0408
    EditText search_edittext;
    Button search_button;

    ArrayList<ImageView> images = new ArrayList<>();
    ArrayList<TextView> texts = new ArrayList<>();
    ArrayList<LinearLayout> cars_layout = new ArrayList<>();

    ArrayList<Button> sites = new ArrayList<>();

    String netip;

    View layout;
    Context mycontext;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //뷰를 가져온다. (findviewmyid 사용)
        layout = inflater.inflate(R.layout.home, container, false);
        //getApplicationContext대신해서 사용
        mycontext = container.getContext();

        netip = getString(R.string.net_ip);

        //검색버튼가기
        main_searchbutton = layout.findViewById(R.id.main_gosearch);
        main_searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_to_Menu();
            }
        });

        TextView home_text1 = layout.findViewById(R.id.home_text1);
        TextView home_text2 = layout.findViewById(R.id.home_text2);
        TextView home_text3 = layout.findViewById(R.id.home_text3);

        //중고차 사이트 바로가기
        TextView sites_text = layout.findViewById(R.id.sites);

        //폰트설정
        AssetManager assets = getResources().getAssets();
        Typeface typeface = Typeface.createFromAsset(assets, "nexon1.otf" );
        home_text1.setTypeface(typeface);
        home_text2.setTypeface(typeface);
        home_text3.setTypeface(typeface);
        sites_text.setTypeface(typeface);
        home_text1.setTextColor(Color.BLACK);
        home_text2.setTextColor(Color.BLACK);
        home_text3.setTextColor(Color.BLACK);
        sites_text.setTextColor(Color.BLACK);

        search_edittext = layout.findViewById(R.id.search_edittext);
        search_button = layout.findViewById(R.id.search_button);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(search_edittext.getText().toString().equals("")){
                    Toast.makeText(mycontext, "키워드를 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    //키워드로 검색
                    //검색 후 키보드 내리기
                    InputMethodManager manager = (InputMethodManager)getContext().getSystemService(INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(search_edittext.getWindowToken(), 0);
                    keyword_search();
                    search_edittext.setText("");
                }
            }
        });

        //가장 많이 조회 된 차량
        int[] imagebox = {R.id.main_image1, R.id.main_image2, R.id.main_image3, R.id.main_image4};
        int[] textbox = {R.id.main_text1, R.id.main_text2, R.id.main_text3, R.id.main_text4};
        int[] carbox = {R.id.car1, R.id.car2, R.id.car3, R.id.car4};
        int[] car_sites = {R.id.home_encar, R.id.home_kb, R.id.home_kcar, R.id.home_bobae};
        for(int i=0;i<4;i++){
            images.add((ImageView) layout.findViewById(imagebox[i]));
            texts.add((TextView) layout.findViewById(textbox[i]));
            cars_layout.add(layout.findViewById(carbox[i]));
            sites.add(layout.findViewById(car_sites[i]));
        }

        //레이아웃의 테두리 설정
        for(int i=0;i<4;i++){
            cars_layout.get(i).setBackgroundResource(R.drawable.border_bottom);

            //마진을 쓸려면  아래와 같은 방법을 쓰면 된다.
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            param.leftMargin = 10;
            param.rightMargin = 10;
            cars_layout.get(i).setLayoutParams(param);
            texts.get(i).setLayoutParams(param);
        }

        String[] site_address = {
                "https://m.encar.com/index.do",
                "https://m.kbchachacha.com/",
                "https://m.kcar.com/mobile/search/index.do",
                "https://m.bobaedream.co.kr/"
        };

        //버튼설정
        for(int i=0;i<4;i++){
            int finalI = i;
            sites.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(site_address[finalI])); startActivity(intent);
                }
            });
        }

        _okhttp_connect_get net = new _okhttp_connect_get();
        net.site = "http://"+ netip + "/car_by_views.php";
        net.makesales();

        ExchangeFormat ef = new ExchangeFormat();
        ExchangeFormat_1000 ef1000 = new ExchangeFormat_1000();

        net.start();
        try {
            net.join(5000);

            ArrayList<ArrayList> sales = net.get_sales();
            for(int i=0;i<4;i++){
                network_bitmap bitmap_thread = new network_bitmap();
                bitmap_thread.site = (String) "http://"+ netip + "/" + sales.get(15).get(i);
                bitmap_thread.start();
                try {
                    bitmap_thread.join();
                    images.get(i).setImageBitmap(bitmap_thread.bit());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                texts.get(i).setText(sales.get(2).get(i).toString()+"\n"+sales.get(3).get(i).toString()
                +"\n"+ef1000.unit_change(sales.get(10).get(i).toString()) + "KM"+ "\n" + ef.unit_change(sales.get(9).get(i).toString()) + "원");

                int finalI = i;
                cars_layout.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //인텐트전환
                        Intent nextIntent = new Intent(mycontext, Search_Result_InfomationActivity.class);
                        nextIntent.putExtra("url_address", sales.get(1).get(finalI).toString());//0
                        nextIntent.putExtra("sitetype", sales.get(2).get(finalI).toString()); //1
                        nextIntent.putExtra("car_title", sales.get(3).get(finalI).toString()); //2
                        nextIntent.putExtra("carnumber", sales.get(4).get(finalI).toString()); //3
                        nextIntent.putExtra("cartype", sales.get(5).get(finalI).toString()); //4
                        nextIntent.putExtra("manufacturer", sales.get(6).get(finalI).toString()); //5
                        nextIntent.putExtra("model", sales.get(7).get(finalI).toString()); //6
                        nextIntent.putExtra("model_detail", sales.get(8).get(finalI).toString()); //7
                        nextIntent.putExtra("price", sales.get(9).get(finalI).toString()); //8
                        nextIntent.putExtra("distance", sales.get(10).get(finalI).toString()); //9
                        nextIntent.putExtra("caryear", sales.get(12).get(finalI).toString()); //10
                        nextIntent.putExtra("carcolor", sales.get(13).get(finalI).toString()); //11
                        nextIntent.putExtra("carfuel", sales.get(14).get(finalI).toString()); //12
                        nextIntent.putExtra("imglink",  sales.get(15).get(finalI).toString()); //13
                        nextIntent.putExtra("car_id", sales.get(0).get(finalI).toString()); //14
                        startActivity(nextIntent);
                    }
                });
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException k){
            mainactivity.disconnected();
        }
        return layout;
    }

    MainActivity mainactivity;

    public void change_to_Menu(){
        if(mainactivity.bottom11==null){
            mainactivity.bottom11 = new Search();
            mainactivity.fragmentManager.beginTransaction().add(R.id.main_frame, mainactivity.bottom11).commit();
        }

        if(mainactivity.bottom1!=null) mainactivity.fragmentManager.beginTransaction().hide(mainactivity.bottom1).commit();
        if(mainactivity.bottom11!=null) {
            if(mainactivity.bottom111!=null){
                mainactivity.fragmentManager.beginTransaction().show(mainactivity.bottom111).commit();
            }
            else{
                mainactivity.fragmentManager.beginTransaction().show(mainactivity.bottom11).commit();
            }
        }
        if(mainactivity.bottom2!=null) mainactivity.fragmentManager.beginTransaction().hide(mainactivity.bottom2).commit();
        if(mainactivity.bottom3!=null) mainactivity.fragmentManager.beginTransaction().hide(mainactivity.bottom3).commit();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainactivity=(MainActivity)getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainactivity=null;
    }


    Bundle bundle = new Bundle();
    public void keyword_search(){
        if(mainactivity.search_resultActivity_keyword==null){

            mainactivity.search_resultActivity_keyword = new Search_ResultActivity_keyword();
            mainactivity.search_result_activity_keyword_name = search_edittext.getText().toString();
            bundle.putString("keyword", search_edittext.getText().toString());
            mainactivity.search_resultActivity_keyword.setArguments(bundle);
            mainactivity.fragmentManager.beginTransaction().add(R.id.main_frame, mainactivity.search_resultActivity_keyword).commit();
        }
        if(mainactivity.bottom1!=null) mainactivity.fragmentManager.beginTransaction().hide(mainactivity.bottom1).commit();
        if(mainactivity.bottom11!=null) mainactivity.fragmentManager.beginTransaction().hide(mainactivity.bottom11).commit();
        if(mainactivity.bottom111!=null) mainactivity.fragmentManager.beginTransaction().hide(mainactivity.bottom111).commit();
        if(mainactivity.search_resultActivity_keyword!=null) mainactivity.fragmentManager.beginTransaction().show(mainactivity.search_resultActivity_keyword).commit();
        if(mainactivity.bottom2!=null) mainactivity.fragmentManager.beginTransaction().hide(mainactivity.bottom2).commit();
        if(mainactivity.bottom3!=null) mainactivity.fragmentManager.beginTransaction().hide(mainactivity.bottom3).commit();
    }
}