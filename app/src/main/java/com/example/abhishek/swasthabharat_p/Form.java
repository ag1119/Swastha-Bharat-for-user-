package com.example.abhishek.swasthabharat_p;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class Form extends AppCompatActivity {

    WebView webView;
    String url="https://docs.google.com/forms/d/e/1FAIpQLSfHCXwEXxggESuZU-x3NVJzBNo5ZEnsHazhKQdkUXUfSSQlcg/formResponse";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        webView=(WebView)findViewById(R.id.webView);
        webView.getSettings().getJavaScriptEnabled();
        webView.loadUrl(url);
    }
}
