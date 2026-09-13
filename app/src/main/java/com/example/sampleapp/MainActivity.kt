package com.example.androidapkbuilder;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {

    private WebView webView;
    private ProgressBar loader;

    final String[][] channelList = {
        {"T Sports Live", "https://freestreams-live1.tv/t-sports/"},
        {"Sony Ten 1", "https://freestreams-live1.tv/sony-ten-1/"},
        {"Fox Sports 1", "https://freestreams-live1.tv/fox-sports-1/"},
        {"beIN Sports 1", "https://freestreams-live1.tv/bein-sports-1/"},
        {"Willow Cricket", "https://freestreams-live1.tv/willow-cricket/"},
        {"Sky Sports HD", "https://freestreams-live1.tv/sky-sports-football/"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setBackgroundColor(Color.parseColor("#121212"));

        TextView header = new TextView(this);
        header.setText("RAFIM LIVE SPORTS");
        header.setTextColor(Color.WHITE);
        header.setTextSize(20);
        header.setBackgroundColor(Color.parseColor("#E50914"));
        header.setPadding(35, 35, 35, 35);
        rootLayout.addView(header);

        FrameLayout playerFrame = new FrameLayout(this);
        LinearLayout.LayoutParams playerParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 650);
        playerFrame.setLayoutParams(playerParams);
        playerFrame.setBackgroundColor(Color.BLACK);

        webView = new WebView(this);
        webView.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        loader = new ProgressBar(this);
        FrameLayout.LayoutParams loaderParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loaderParams.gravity = Gravity.CENTER;
        loader.setLayoutParams(loaderParams);

        playerFrame.addView(webView);
        playerFrame.addView(loader);
        rootLayout.addView(playerFrame);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                loader.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                loader.setVisibility(View.GONE);
                view.loadUrl("javascript:(function() { " +
                        "document.getElementsByTagName('header')[0].style.display='none'; " +
                        "document.getElementsByTagName('footer')[0].style.display='none'; " +
                        "})()");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("freestreams-live1.tv") || url.contains("m3u8") || url.contains("cdn")) {
                    return false;
                }
                return true;
            }
        });

        GridView gridView = new GridView(this);
        gridView.setNumColumns(2);
        gridView.setHorizontalSpacing(15);
        gridView.setVerticalSpacing(15);
        gridView.setPadding(20, 20, 20, 20);

        gridView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() { return channelList.length; }

            @Override
            public Object getItem(int i) { return channelList[i]; }

            @Override
            public long getItemId(int i) { return i; }

            @Override
            public View getView(final int i, View view, ViewGroup viewGroup) {
                TextView btn = new TextView(MainActivity.this);
                btn.setText(channelList[i][0]);
                btn.setTextColor(Color.WHITE);
                btn.setTextSize(16);
                btn.setGravity(Gravity.CENTER);
                btn.setBackgroundColor(Color.parseColor("#222222"));
                btn.setPadding(20, 45, 20, 45);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loader.setVisibility(View.VISIBLE);
                        webView.loadUrl(channelList[i][1]);
                    }
                });
                return btn;
            }
        });

        rootLayout.addView(gridView);
        setContentView(rootLayout);

        webView.loadUrl(channelList[0][1]);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
