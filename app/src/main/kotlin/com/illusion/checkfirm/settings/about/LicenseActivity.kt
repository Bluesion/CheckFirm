package com.illusion.checkfirm.settings.about

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.trusted.TrustedWebActivityIntentBuilder
import com.google.android.material.appbar.AppBarLayout
import com.google.androidbrowserhelper.trusted.TwaLauncher
import com.illusion.checkfirm.R
import com.illusion.checkfirm.databinding.ActivityLicenseBinding
import com.illusion.checkfirm.etc.WebViewActivity

class LicenseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLicenseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLicenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()

        binding.apache.setOnClickListener {
            val link = "http://www.apache.org/licenses/LICENSE-2.0.html"
            try {
                val builder = TrustedWebActivityIntentBuilder(Uri.parse(link))
                TwaLauncher(this).launch(builder, null, null)
            } catch (e: ActivityNotFoundException) {
                val intent = Intent(this, WebViewActivity::class.java)
                intent.putExtra("url", link)
                intent.putExtra("number", 2)
                startActivity(intent)
            }
        }

        binding.mit.setOnClickListener {
            val link = "https://opensource.org/licenses/MIT"
            try {
                val builder = TrustedWebActivityIntentBuilder(Uri.parse(link))
                TwaLauncher(this).launch(builder, null, null)
            } catch (e: ActivityNotFoundException) {
                val intent = Intent(this, WebViewActivity::class.java)
                intent.putExtra("url", link)
                intent.putExtra("number", 2)
                startActivity(intent)
            }
        }
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initToolbar() {
        setSupportActionBar(binding.includeToolbar.toolbar)
        val toolbarText = getString(R.string.license)
        val title = binding.includeToolbar.title
        title.text = toolbarText
        val expandedTitle = binding.includeToolbar.expandedTitle
        expandedTitle.text = toolbarText

        val mAppBar = binding.includeToolbar.appbar
        mAppBar.layoutParams.height = (resources.displayMetrics.heightPixels * 0.3976).toInt()
        mAppBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, _ ->
            val percentage = (appBarLayout.y / appBarLayout.totalScrollRange)
            expandedTitle.alpha = 1 - (percentage * 2 * -1)
            title.alpha = percentage * -1
        })

        val one = getSharedPreferences("settings", Context.MODE_PRIVATE).getBoolean("one", true)
        if (one) {
            mAppBar.setExpanded(true)
        } else {
            mAppBar.setExpanded(false)
        }
    }
}