package com.example.rss;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;

public class Browser extends AppCompatActivity {
    private WebView browser;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browser);
        url = getIntent().getStringExtra("url");
        try
        {
            browser = findViewById(R.id._browser);
            browser.setWebViewClient(new webClient());
            browser.getSettings().setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
            browser.getSettings().setAllowFileAccess(true);
            browser.getSettings().setAppCacheEnabled(true);
            browser.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
            NetworkInfo networkInfo = ((ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            boolean networkStatus = networkInfo != null && networkInfo.isConnectedOrConnecting();
            if (!networkStatus)
            {
                browser.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            }
            browser.loadUrl(url);
        }
        catch (Exception ex)
        {

        }
    }

    @Override
    public void onBackPressed()
    {
        if(browser.canGoBack())
        {
            browser.goBack();
        }
        else
        {
            super.onBackPressed();
        }
    }
}
