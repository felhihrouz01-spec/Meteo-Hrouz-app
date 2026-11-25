package com.wMeteoHrouz_20422910;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);

        LinearLayout topBar = new LinearLayout(this);
        topBar.setBackgroundColor(Color.parseColor("#0d1b2a"));
        topBar.setPadding(32, 24, 32, 24);
        topBar.setGravity(Gravity.CENTER_VERTICAL);

        TextView title = new TextView(this);
        title.setText("MeteoHrouz 🌤️");
        title.setTextColor(Color.WHITE);
        title.setTextSize(18);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        title.setLayoutParams(titleParams);

        TextView shareBtn = new TextView(this);
        shareBtn.setText("📤 مشاركة");
        shareBtn.setTextColor(Color.parseColor("#4caf50"));
        shareBtn.setTextSize(15);
        shareBtn.setPadding(24, 10, 24, 10);
        shareBtn.setOnClickListener(v -> shareApp());

        topBar.addView(title);
        topBar.addView(shareBtn);

        swipeRefresh = new SwipeRefreshLayout(this);
        webView = new WebView(this);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                swipeRefresh.setRefreshing(false);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });

        swipeRefresh.setOnRefreshListener(() -> webView.reload());
        swipeRefresh.addView(webView);

        root.addView(topBar);
        root.addView(swipeRefresh, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        setContentView(root);
        webView.loadUrl("https://github.io");
    }

    private void shareApp() {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "تطبيق MeteoHrouz للطقس والإرشاد الفلاحي");
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "🌾 تابع حالة الطقس، رادار الأمطار، ونصائح الري والزراعة يومياً عبر تطبيق MeteoHrouz:\nhttps://github.io");
        startActivity(Intent.createChooser(sendIntent, "مشاركة MeteoHrouz عبر"));
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
