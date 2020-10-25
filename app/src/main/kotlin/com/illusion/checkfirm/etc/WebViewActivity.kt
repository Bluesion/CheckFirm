package com.illusion.checkfirm.etc

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.illusion.checkfirm.R
import com.illusion.checkfirm.databinding.ActivityWebviewBinding

class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val number = intent.getIntExtra("number", 1)
        val url = intent.getStringExtra("url")

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        if (number == 1) {
            binding.toolbar.title = getString(R.string.changelog)
        } else {
            binding.toolbar.title = getString(R.string.license)
        }

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.defaultTextEncodingName = "utf-8"
        binding.webView.settings.useWideViewPort = true
        binding.webView.settings.setSupportZoom(true)
        binding.webView.settings.builtInZoomControls = true
        binding.webView.webChromeClient = WebChromeClient()
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                view.loadUrl(url!!)
                return true
            }
        }
        binding.webView.loadUrl(url!!)
    }
}
