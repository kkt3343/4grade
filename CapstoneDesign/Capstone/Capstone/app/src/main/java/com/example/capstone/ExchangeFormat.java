package com.example.capstone;

import java.text.DecimalFormat;

//숫자를 변경하는 클래스 22.03.07
//EX 154782600032546 -> 154조7826억3만2346

public class ExchangeFormat {
    public String unit_change(String number) {
        DecimalFormat d = new DecimalFormat("#,####");

        int tmp = Integer.parseInt(number);

        String[] unit = new String[]{"", "만", "억", "조"};
        String[] str = d.format(tmp).split(",");
        String result = "";
        int cnt = 0;
        for (int i = str.length; i > 0; i--) {
            if (Integer.parseInt(str[i - 1]) != 0) {
                result = String.valueOf(Integer.parseInt(str[i - 1])) + unit[cnt] + result;
            }
            cnt++;
        }
        return result;
    }

    public String unit_rollback(String number) {
        long eok = 100000000;
        long man = 10000;

        String tmp = "";
        long result = 0;

        for (int i = 0; i<number.length(); i++) {
            if(Character.isDigit(number.charAt(i))){
                tmp = tmp + number.charAt(i);
            }else {
                if(number.charAt(i) == '억') {
                    result = result + Integer.parseInt(tmp) * eok;
                    tmp = "";
                }
                else if(number.charAt(i) == '만') {
                    result = result + Integer.parseInt(tmp) * man;
                    tmp = "";
                }
                else {

                }
            }

        }
        return String.valueOf(result);
    }
}