package com.ng.ui;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-01-07
 */
public class WebViewTestActivity extends Activity {
    WebView wb_my;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_webview);
        wb_my = findViewById(R.id.wb_my);
        //设置允许执行JS，默认是false
        wb_my.getSettings().setJavaScriptEnabled(true);
        //WebViewClient可以处理请求和通知,这里是防止系统浏览器加载页面
        wb_my.setWebViewClient(new WebViewClient());
        //加载设置的网页
        wb_my.loadUrl("https://act.webull.com/contentEdit/index.html?pageId=3518");
    }
}
