package com.example.abhishek.swasthabharat_p;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FleshPage extends AppCompatActivity implements Runnable {

    public static final String PREF="myNumber";
    String myNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flesh_page);
        SharedPreferences sf=getSharedPreferences(PREF,MODE_PRIVATE);
        myNumber=sf.getString("myNumber","null");
        Thread main =new Thread(this);
        main.start();
    }

    @Override
    public void run() {
        try{
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finish();
        if(myNumber.equals("null"))
          startActivity(new Intent(FleshPage.this,LoginPage.class));
        else
            startActivity(new Intent(FleshPage.this,MainPage.class));
    }
}
