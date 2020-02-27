package kovalenkoilya.fit.bstu.lab5vbb;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class Browser extends Activity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        webView = (WebView) findViewById(R.id.wv);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }
}
