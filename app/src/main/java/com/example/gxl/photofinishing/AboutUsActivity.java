package com.example.gxl.photofinishing;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Administrator on 2016/6/13 0013.
 */
public class AboutUsActivity extends Activity {
    public WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_syncbackup);
        webView= (WebView) findViewById(R.id.webview);
//        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
//        webView.getSettings().setBlockNetworkImage(true);
//        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
//        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl("http://idhut.cn/tfsite/funpic/about.html");
    }
}
