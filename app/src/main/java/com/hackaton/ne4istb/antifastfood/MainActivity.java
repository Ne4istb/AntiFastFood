package com.hackaton.ne4istb.antifastfood;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity implements View.OnTouchListener {

    public static final String FIRST_RUN_PREFERENCE = "dearHamster.firstRun";

    WebSettings wSettings;
    boolean redirected = false;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        actionBar.hide();

        onFirstRun();

        WebView webView = new WebView(this);

        webView.setOnTouchListener(this);

        webView.setClickable(true);
        wSettings = webView.getSettings();
        wSettings.setJavaScriptEnabled(true);

        WebViewClient webViewClient = new WebViewClient();
        webView.setWebViewClient(webViewClient);
        WebChromeClient webChromeClient = new WebChromeClient();
        webView.setWebChromeClient(webChromeClient);

        webView.loadUrl("file:///android_asset/myhtml.html");

        setContentView(webView);

        redirected = false;
    }

    private void onFirstRun() {

        SharedPreferences preference = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        boolean firstRun = preference.getBoolean(FIRST_RUN_PREFERENCE, true);
//        if (firstRun || UtilsHelper.DEBUG) {
        if (firstRun) {
            startService(new Intent(this, GeolocationService.class));
            preference.edit().putBoolean(FIRST_RUN_PREFERENCE, false).commit();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.e("GEO", "test");
        if (!redirected) {
            Intent intent = new Intent(this, SuggestionActivity.class);
            startActivity(intent);
            redirected = true;
        }
        return false;
    }
}