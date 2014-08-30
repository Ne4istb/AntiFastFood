package com.hackaton.ne4istb.antifastfood;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

    public static final String FIRST_RUN_PREFERENCE = "dearHamster.firstRun";

    WebSettings wSettings;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        onFirstRun();

        WebView webView = new WebView(this);
        webView.setClickable(true);
        wSettings = webView.getSettings();
        wSettings.setJavaScriptEnabled(true);

        WebClientClass webViewClient = new WebClientClass();
        webView.setWebViewClient(webViewClient);
        WebChromeClient webChromeClient = new WebChromeClient();
        webView.setWebChromeClient(webChromeClient);

        webView.addJavascriptInterface(new myJsInterface(this), "Android");

        webView.loadUrl("file:///android_asset/myhtml.html");
        setContentView(webView);
    }

    private void onFirstRun() {

//        SharedPreferences preference = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
//        boolean firstRun = preference.getBoolean(FIRST_RUN_PREFERENCE, true);
//        if (firstRun || UtilsHelper.DEBUG) {
//        if (firstRun) {
        startService(new Intent(this, GeolocationService.class));
//            preference.edit().putBoolean(FIRST_RUN_PREFERENCE, false).commit();
//        }
    }

    public class WebClientClass extends WebViewClient {
        ProgressDialog pd = null;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            pd = new ProgressDialog(MainActivity.this);
            pd.setTitle("Please wait");
            pd.setMessage("Page is loading..");
            pd.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            pd.dismiss();
        }
    }

    public class myJsInterface {

        private Context con;

        public myJsInterface(Context con) {
            this.con = con;
        }

        public void showToast(String mssg) {
            AlertDialog alert = new AlertDialog.Builder(con)
                    .create();
            alert.setTitle("My Js Alert");
            alert.setMessage(mssg);
            alert.setButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

        }
    }
}