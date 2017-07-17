package com.sinto.sinto.visitour2.webview;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sinto.sinto.visitour2.R;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        WebView view = (WebView) findViewById(R.id.web_view_widget);
        view.setWebViewClient(new WebViewClient());
        view.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                view.loadUrl("javascript:(function() {" +
                        "var els = document.getElementsByTagName('fb-appbar');" +
                        "for (var i=0; i < els.length; i++) {els[i].style.display = 'none'}" +
                        "var els = document.getElementsByTagName('fb-feature-bar');" +
                        "for (var i=0; i < els.length; i++) {els[i].style.display = 'none'}" +
                        "var els = document.getElementsByClassName('interactive-url-toolbar');" +
                        "for (var i=0; i < els.length; i++) {els[i].style.display = 'none'}" +
                        "var els = document.getElementsByClassName('data-tree-no-icons');" +
                        "for (var i=0; i < els.length; i++) {" +
                        "  els[i].style.position='fixed';" +
                        "  els[i].style['z-index']=100;" +
                        "  els[i].style['background-color']='white';" +
                        "  els[i].style.top=0;" +
                        "  els[i].style.bottom=0;" +
                        "  els[i].style.left=0;" +
                        "  els[i].style.right=0;" +
                        "}" +
                        "})()");
            }
        });
        view.getSettings().setJavaScriptEnabled(true);
        view.getSettings().setBuiltInZoomControls(true);
        view.setVerticalScrollBarEnabled(true);
        view.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        view.setScrollbarFadingEnabled(false);
        view.loadUrl("https://console.firebase.google.com/project/visitour-2a6d8/database/data");
    }

}
