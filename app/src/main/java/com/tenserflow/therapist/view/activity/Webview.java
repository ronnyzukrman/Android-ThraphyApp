package com.tenserflow.therapist.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tenserflow.therapist.R;

/**
 * Created by Admin on 12/18/2018.
 */

public class Webview extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        WebView webView = (WebView) findViewById(R.id.webview);
        RelativeLayout backpress = (RelativeLayout) findViewById(R.id.header_webview);
        TextView terms_and_policy = (TextView) findViewById(R.id.terms_and_policy);


        Intent intent = getIntent();
        if (intent!=null)
        {
            if (intent.hasExtra("way"))
            {
                String way = intent.getStringExtra("way");
                if (way.equalsIgnoreCase("TermsOfUse"))
                {
                    terms_and_policy.setText("Terms Of Use");

                    WebSettings webSettings = webView.getSettings();
                    webSettings.setJavaScriptEnabled(true);
                    webView.loadUrl("http://therapy.gangtask.com/terms.php");

                }
                else
                {
                    terms_and_policy.setText("Privacy Policy");

                    WebSettings webSettings = webView.getSettings();
                    webSettings.setJavaScriptEnabled(true);
                    webView.loadUrl("http://therapy.gangtask.com/policy.php");
                }
            }
        }



        backpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
