package com.example.capstone;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class Idcheck_fail extends Activity {

    private TextView text;
    private Button confirm;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.idcheck_fail);

        //데이터 가져오기
        Intent secondIntent = getIntent();
        String result = secondIntent.getStringExtra("my_id");
        String resultgive = result;
        int textlength = result.length();

        result = result + "는 중복된 아이디 입니다.";

        final SpannableStringBuilder sp = new SpannableStringBuilder(result);
        sp.setSpan(new ForegroundColorSpan(Color.rgb(204,61,61)), 0, textlength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, textlength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); //bold
        sp.setSpan(new AbsoluteSizeSpan(50), 0, textlength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//resize size


        //텍스트
        text = findViewById(R.id.txtText);
        text.setText(sp, TextView.BufferType.SPANNABLE);

        //버튼
        confirm = findViewById(R.id.confirm);

        // 확인버튼
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //데이터 전달하기
                Intent intent = new Intent();
                intent.putExtra("result", resultgive.toString());
                setResult(RESULT_OK, intent);

                //액티비티(팝업) 닫기
                finish();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}