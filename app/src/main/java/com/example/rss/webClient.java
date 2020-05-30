package com.example.rss;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class webClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}
