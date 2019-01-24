package com.example.abhishek.swasthabharat_p;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

public class LoginPage extends AppCompatActivity {
    public static final String PREF="myNumber";
    TextInputEditText phone;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        phone=(TextInputEditText)findViewById(R.id.phone);
        login=(Button)findViewById(R.id.loginBtn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number=phone.getText().toString();
                if(!TextUtils.isEmpty(number)&&number.length()==10)
                {
                    SharedPreferences sf=getSharedPreferences(PREF,MODE_PRIVATE);
                    SharedPreferences.Editor editor=sf.edit();
                    editor.putString("myNumber",number);
                    editor.commit();
                    finish();
                    Intent intent=new Intent(LoginPage.this,MainPage.class);
                    startActivity(intent);
                }
            }
        });
    }
}
