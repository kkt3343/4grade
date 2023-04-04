package com.example.capstone;

import java.text.DecimalFormat;

//숫자 천단위로 콤마 찍기
public class ExchangeFormat_1000{
    public String unit_change(String number){
        int tmp = Integer.parseInt(number);
        DecimalFormat df = new DecimalFormat("###,###");
        return df.format(tmp);
    }
}