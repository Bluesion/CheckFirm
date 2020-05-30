package com.illusion.checkfirm.etc

import android.os.Bundle
import android.webkit.WebSettings
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

        val webView = binding.webView
        webView.settings.setAppCacheEnabled(true)
        webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
        webView.settings.setAppCachePath(cacheDir.path)
        webView.webViewClient = WebViewClient()
        webView.loadUrl(url)
    }
}