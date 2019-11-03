package com.illusion.checkfirm.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.illusion.checkfirm.R

@SuppressLint("SetJavaScriptEnabled")
class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        val number = intent.getIntExtra("number", 1)
        val url = intent.getStringExtra("url")

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        if (number == 1) {
            toolbar.title = getString(R.string.changelog)
        } else {
            toolbar.title = getString(R.string.license)
        }

        val webView = findViewById<WebView>(R.id.webview)
        webView.settings.javaScriptEnabled = true
        webView.settings.setAppCacheEnabled(true)
        webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
        webView.settings.setAppCachePath(cacheDir.path)
        webView.webViewClient = WebViewClient()
        webView.loadUrl(url)
    }
}