package com.bct071.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class OfflineView extends AppCompatActivity {

    TextView title;
    WebView web;
    Button b1,save;
    //String htmlString="<h3>Connection Not Available</h3><br><br> <p><h5> Please connect to internet and try again. . . . .</h5></p>";


    String source_code,title_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarr);
        setSupportActionBar(toolbar);
        source_code=getIntent().getStringExtra("_source");
        title_text=getIntent().getStringExtra("_title");


        b1=(Button)findViewById(R.id.share_button);
        save=(Button)findViewById(R.id.save_button);
        b1.setVisibility(View.GONE);
        save.setVisibility(View.GONE);


        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                    finish();

            }
        });
        //toolbar.setTitle(content);
        //toolbar.setSubtitle(content);


       // url=getIntent().getStringExtra("url");

        title=(TextView)findViewById(R.id.textView);
        web=(WebView)findViewById(R.id.webView);
        WebSettings webSettings=web.getSettings();
        webSettings.setDefaultFontSize(18);
        //web.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        //web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
     //   webSettings.setBlockNetworkImage(true);
        //webSettings.setLoadsImagesAutomatically(true);
        //webSettings.setGeolocationEnabled(false);
        webSettings.setNeedInitialFocus(false);
        webSettings.setSaveFormData(false);




        //   MobileAds.initialize(getApplicationContext(), "ca-app-pub-5708324311795426/8036178998");

        title.setText(title_text);
        //web.getSettings().setJavaScriptEnabled(true);
        web.loadDataWithBaseURL("",source_code, "text/html", "UTF-8", "");



    }

}
