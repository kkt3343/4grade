package com.example.capstone;

import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FCM_token extends Thread{
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


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
