package com.example.capstone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.Arrays;

public class Search_frag_R extends Fragment{
    private String netip;
    //스피너 데이터를 저장할 ArrayList
    ArrayList<String> spinner_text1 = new ArrayList<>();
    ArrayList<String> spinner_text2 = new ArrayList<>();
    ArrayList<String> spinner_text3 = new ArrayList<>();

    //getApplicationContext대신해서 사용
    private Context context;

    Button resetbtn;

    //뷰를 가져온다. (findviewmyid 사용)
    View layout;

    Search_ResultActivity search;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //뷰를 가져온다. (findviewmyid 사용)
        layout = inflater.inflate(R.layout.search_frag_right, container, false);
        //getApplicationContext대신해서 사용
        context = container.getContext();

        //아이피주소받아오기
        netip = getString(R.string.net_ip);

        //서버에서 데이터 받아오기
        networkthread net = new networkthread();

        //제조사 받아오기(수입차)
        net.site = "http://"+netip+"/car_manufacturer_i.php";
        Log.d("아이피",net.site);
        net.keyword = "manufacturer";
        net.start();
        try {
            net.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        spinner_text1 = net.text();
        spinner_text1.addAll(0, new ArrayList<String>(Arrays.asList("제조사를 선택하세요")));

        //모델명 & 세부 모델명 텍스트뷰
        TextView manufacturer_text2 = layout.findViewById(R.id.search_nouse2);
        TextView manufacturer_text3 = layout.findViewById(R.id.search_nouse3);

        //제조사 검색하기
        Spinner manufacturer_spinner = layout.findViewById(R.id.spinner_manufacturer);
        ArrayAdapter<String> manufacturer_spinner_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, spinner_text1);
        manufacturer_spinner_adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        manufacturer_spinner.setAdapter(manufacturer_spinner_adapter);

        //스피너 부분은 여기서 처리한다.
        //검색형 스피너설정
        SearchableSpinner searchspinner = layout.findViewById(R.id.spinner_manufacturer);
        searchspinner.setTitle("수입제조사 선택하기");
        searchspinner.setPositiveButton("완료");

        //모델명 검색하기
        Spinner manufacturer_spinner2 = layout.findViewById(R.id.spinner_modelname);
        ArrayAdapter<String> manufacturer_spinner_adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, spinner_text2);
        manufacturer_spinner_adapter2.setDropDownViewResource(android.R.layout.simple_list_item_1);
        manufacturer_spinner2.setAdapter(manufacturer_spinner_adapter2);

        //세부 모델명 검색하기
        Spinner manufacturer_spinner3 = layout.findViewById(R.id.spinner_modelname_detail);
        ArrayAdapter<String> manufacturer_spinner_adapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, spinner_text3);
        manufacturer_spinner_adapter3.setDropDownViewResource(android.R.layout.simple_list_item_1);
        manufacturer_spinner3.setAdapter(manufacturer_spinner_adapter3);

        //위치 초기화
        manufacturer_spinner.setSelection(0);
        manufacturer_spinner2.setSelection(0);
        manufacturer_spinner3.setSelection(0);
        //가져오고 일단 숨겨둔다.
        manufacturer_spinner2.setVisibility(View.INVISIBLE);
        manufacturer_text2.setVisibility(View.INVISIBLE);
        manufacturer_spinner3.setVisibility(View.INVISIBLE);
        manufacturer_text3.setVisibility(View.INVISIBLE);

        resetbtn = layout.findViewById(R.id.reset);
        resetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manufacturer_spinner.setSelection(0);
                manufacturer_spinner2.setSelection(0);
                manufacturer_spinner3.setSelection(0);
            }
        });

        //제조사 검색하기 부분
        manufacturer_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    // 만약에 다른 선택된 값이 생긴다면 다음 카테고리를 보여주게 한다.
                    manufacturer_spinner2.setVisibility(View.VISIBLE);
                    manufacturer_text2.setVisibility(View.VISIBLE);

                    //선택된 값으로 다시 한번 통신하기
                    networkthread net2 = new networkthread();
                    net2.site = "http://"+netip+"/car_model_i.php?manufacturer="+manufacturer_spinner.getSelectedItem().toString();

                    net2.keyword = "model";
                    net2.start();

                    try {
                        net2.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    spinner_text2 = net2.text();

                    manufacturer_spinner_adapter2.clear();

                    spinner_text2.addAll(0,new ArrayList<String>(Arrays.asList("모델명을 선택하세요")));

                    manufacturer_spinner_adapter2.addAll(spinner_text2);
                    manufacturer_spinner_adapter2.notifyDataSetChanged();

                    //위치 초기화
                    manufacturer_spinner2.setSelection(0);
                }
                else{
                    manufacturer_spinner2.setVisibility(View.INVISIBLE);
                    manufacturer_text2.setVisibility(View.INVISIBLE);
                    manufacturer_spinner3.setVisibility(View.INVISIBLE);
                    manufacturer_text3.setVisibility(View.INVISIBLE);

                    //위치 초기화
                    manufacturer_spinner2.setSelection(0);
                    manufacturer_spinner3.setSelection(0);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //모델명 검색하기 부분
        manufacturer_spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0){
                    // 만약에 다른 선택된 값이 생긴다면 다음 카테고리를 보여주게 한다.
                    manufacturer_spinner3.setVisibility(View.VISIBLE);
                    manufacturer_text3.setVisibility(View.VISIBLE);

                    //가져온 모델명으로 모델명 세부를 다시한번 통신하기
                    networkthread net3 = new networkthread();
                    //http://59.17.200.134/car_model_detail.php?manufacturer=현대&model=쏘나타
                    net3.site = "http://"+netip+"/car_model_detail_i.php?manufacturer="+manufacturer_spinner.getSelectedItem().toString()+"&model="+manufacturer_spinner2.getSelectedItem().toString();

                    net3.keyword = "model_detail";
                    net3.start();

                    try {
                        net3.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    spinner_text3 = net3.text();

                    manufacturer_spinner_adapter3.clear();

                    ArrayList<String> tmp3 = new ArrayList<String>(Arrays.asList("모델명 세부사항을 선택하세요"));
                    spinner_text3.addAll(0,tmp3);

                    manufacturer_spinner_adapter3.addAll(spinner_text3);
                    manufacturer_spinner_adapter3.notifyDataSetChanged();

                    //위치 초기화
                    manufacturer_spinner3.setSelection(0);
                }
                else{
                    manufacturer_spinner3.setVisibility(View.INVISIBLE);
                    manufacturer_text3.setVisibility(View.INVISIBLE);

                    //위치 초기화
                    manufacturer_spinner3.setSelection(0);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //모델명 세부 검색하기 부분
        manufacturer_spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0){
                }
                else{
                    //위치 초기화
                    manufacturer_spinner3.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //검색완료
        Button search_complete_button = layout.findViewById(R.id.search_complete);
        ArrayList<String> finalText = spinner_text1;

        search = new Search_ResultActivity();

        search_complete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //예를들어 현대만 누르면 현대만 출력해주는 기능
                //Toast.makeText(getApplicationContext(), manufacturer_spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();

                if (manufacturer_spinner.getSelectedItem().toString() == "제조사를 선택하세요"){
                    Toast.makeText(context, "키워드를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    bundle.putString("manufacturer", manufacturer_spinner.getSelectedItem().toString());

                    //Intent nextIntent = new Intent(context, Search_ResultActivity.class);
                    //첫번째 값
                    //nextIntent.putExtra("manufacturer",manufacturer_spinner.getSelectedItem().toString());

                    //두번째 값
                    if (manufacturer_spinner2.getSelectedItem().toString() == "모델명을 선택하세요"){
//                        nextIntent.putExtra("model","NULL");
//                        nextIntent.putExtra("model_detail","NULL");

                        bundle.putString("model", "NULL");
                        bundle.putString("model_detail", "NULL");
                    }
                    else{
                        //nextIntent.putExtra("model",manufacturer_spinner2.getSelectedItem().toString());
                        bundle.putString("model", manufacturer_spinner2.getSelectedItem().toString());
                        if (manufacturer_spinner3.getSelectedItem().toString() == "모델명 세부사항을 선택하세요"){
                            //nextIntent.putExtra("model_detail","NULL");
                            bundle.putString("model_detail", "NULL");
                        }
                        else{
                            //nextIntent.putExtra("model_detail",manufacturer_spinner3.getSelectedItem().toString());
                            bundle.putString("model_detail", manufacturer_spinner3.getSelectedItem().toString());
                        }
                    }
                    //startActivity(nextIntent);
                    change_to_Menu();
                }
            }
        });
        return layout;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        Spinner manufacturer_spinner = layout.findViewById(R.id.spinner_manufacturer);
        manufacturer_spinner.setSelection(0);
    }

    //다음 프래그먼트를 위함
    MainActivity mainactivity;
    //Fragment 전달을 위한 Bundle
    Bundle bundle = new Bundle();

    public void change_to_Menu(){
        if(mainactivity.bottom111==null){

            mainactivity.bottom111 = new Search_ResultActivity();
            mainactivity.bottom111.setArguments(bundle);
            mainactivity.fragmentManager.beginTransaction().add(R.id.main_frame, mainactivity.bottom111).commit();
        }

        if(mainactivity.bottom1!=null) mainactivity.fragmentManager.beginTransaction().hide(mainactivity.bottom1).commit();
        if(mainactivity.bottom11!=null) mainactivity.fragmentManager.beginTransaction().hide(mainactivity.bottom11).commit();
        if(mainactivity.bottom111!=null) mainactivity.fragmentManager.beginTransaction().show(mainactivity.bottom111).commit();
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
}
