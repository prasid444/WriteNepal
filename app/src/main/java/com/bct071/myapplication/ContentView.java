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

public class ContentView extends AppCompatActivity {
    MasterDbAdapter masterDbAdapter;
    ProgressDialog mProgressDialog;
    String url;
    TextView title;
    WebView web;
    Button b1,save;
    String descris;

    String htmlString="<h3>Connection Not Available</h3><br><br> <p><h5> Please connect to internet and try again. . . . .</h5></p>";
    String titleString="";
    String content;

    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        masterDbAdapter=new MasterDbAdapter(this);
        setContentView(R.layout.content_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarr);
        setSupportActionBar(toolbar);
        content=getIntent().getStringExtra("category");
        descris=getIntent().getStringExtra("description");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-5708324311795426/2092220198");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                finish();
                // beginPlayingGame();
            }
        });

        requestNewInterstitial();

        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
                else {
                    finish();
                }
            }
        });
        toolbar.setTitle(content);
        toolbar.setSubtitle(content);


        url=getIntent().getStringExtra("url");

        title=(TextView)findViewById(R.id.textView);
        web=(WebView)findViewById(R.id.webView);
        WebSettings webSettings=web.getSettings();
        webSettings.setDefaultFontSize(18);
        //web.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        //web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setBlockNetworkImage(true);
        //webSettings.setLoadsImagesAutomatically(true);
        webSettings.setGeolocationEnabled(false);
        webSettings.setNeedInitialFocus(false);
        webSettings.setSaveFormData(false);

        new JsoupListView().execute();

        b1=(Button)findViewById(R.id.share_button);
        save=(Button)findViewById(R.id.save_button);

     //   MobileAds.initialize(getApplicationContext(), "ca-app-pub-5708324311795426/8036178998");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);





    }
    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }


    private class JsoupListView extends AsyncTask<Void, Void, Void> {

        Connection con = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            con = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0")
                    .timeout(180000)
                    .ignoreHttpErrors(true)
                    .followRedirects(true);

            Log.i("TAG", "CONNECTED!!!");

            // Create a progressdialog
            mProgressDialog = new ProgressDialog(ContentView.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Write Nepal");
            // Set progressdialog message
            mProgressDialog.setMessage("Fetching contents...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // arraylist = new ArrayList<HashMap<String, String>>();


            try {
                // Connect to the Website URL

                Document doc = con.get();

                Element title=doc.select(".entry >h2").first();

                doc.select("hr").remove();
                doc.select("p.meta").remove();
                doc.getElementById("writersname").remove();
                doc.select("h2").remove();
                doc.select("a").remove();


                Element html=doc.select("div[class=entry]").first();

               htmlString=html.outerHtml();
               titleString=title.text();



            } catch (IOException e1) {
                e1.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {

            // Close the progressdialog
            // listview.setAdapter(adapter);


            title.setText(titleString);
            web.getSettings().setJavaScriptEnabled(true);
            web.loadDataWithBaseURL("", htmlString, "text/html", "UTF-8", "");
            // listView.setAdapter(dataAdapter);
         // Toast.makeText(ContentView.this,"Data added successfully  "+titleString.length(),Toast.LENGTH_SHORT).show();


            //   mcursor.requery();

            if(titleString.length()>=1){
                save.setVisibility(View.VISIBLE);
                b1.setVisibility(View.VISIBLE);

            }
            else{
                save.setVisibility(View.GONE);
                b1.setVisibility(View.GONE);
            }
            mProgressDialog.dismiss();



            //  dbHelper.close();

        }

    }

    public void share_me(View v){
        Toast.makeText(ContentView.this,"Data added successfully  "+titleString.length(),Toast.LENGTH_SHORT).show();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        startActivity(Intent.createChooser(shareIntent, "Share..."));
    }
    public void save_me(View v){
        Toast.makeText(ContentView.this,"Data saved successfully",Toast.LENGTH_SHORT).show();
        masterDbAdapter.open();
        masterDbAdapter.saveData(titleString,descris,htmlString,url);
        masterDbAdapter.close();

    }


}
