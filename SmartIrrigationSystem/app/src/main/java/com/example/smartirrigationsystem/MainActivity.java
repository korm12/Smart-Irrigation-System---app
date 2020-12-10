package com.example.smartirrigationsystem;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private WebView mywebView;
    private RelativeLayout not_connected;
    boolean not_connected_flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mywebView=(WebView) findViewById(R.id.webview);
        not_connected =findViewById(R.id.not_connected);


        createView();
    }
    public boolean isNetworkAvailable(){

        try{
            ConnectivityManager ConMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = null;
            if(ConMan != null){
                netInfo = ConMan.getActiveNetworkInfo();

            }
            return netInfo.isConnected();
        } catch (Exception e) {
            return false;
        }
    }

    void createView(){

        if(isNetworkAvailable()) {
            not_connected.setVisibility(View.GONE);
            mywebView.setVisibility(View.GONE);
            mywebView.loadUrl("http://192.168.80.149/node/ViewLogs.php");
            WebSettings webSettings=mywebView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            mywebView.setWebViewClient(new WebViewClient(){


                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    super.onReceivedError(view, errorCode, description, failingUrl);
                    if(failingUrl.contains("http://192.168.80.149/node/ViewLogs.php")) {
                        mywebView.setVisibility(View.GONE);
                        not_connected.setVisibility(View.VISIBLE);
                        not_connected_flag = true;
                    }
                    else{
                        not_connected_flag = false;
                    }
                }
                @Override
                public void onPageCommitVisible(WebView view, String url) {
                    if(!not_connected_flag) {
                        mywebView.setVisibility(View.VISIBLE);
                        not_connected.setVisibility(View.GONE);
                        not_connected_flag = false;
                    }else{
                        mywebView.setVisibility(View.GONE);
                        not_connected.setVisibility(View.VISIBLE);
                        not_connected_flag = false;
                    }
                }
            }); // setWebViewClient

        } // end isNetworkAvailable funtion
        else {
            not_connected.setVisibility(View.VISIBLE);
            mywebView.setVisibility(View.GONE);
        }
    }

    public void viewWeb(View view){
        createView();
    }

    @Override
    public void onBackPressed(){
        if(mywebView.canGoBack()) {
            mywebView.goBack();
        }
        else{
            super.onBackPressed();
        }
    }


}