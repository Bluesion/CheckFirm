package com.illusion.checkfirm.settings

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.trusted.TrustedWebActivityIntentBuilder
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textview.MaterialTextView
import com.google.androidbrowserhelper.trusted.TwaLauncher
import com.illusion.checkfirm.R
import com.illusion.checkfirm.search.WebViewActivity

class LicenseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_license)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val apache = findViewById<MaterialTextView>(R.id.apache)
        apache.setOnClickListener {
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

        val mit = findViewById<MaterialTextView>(R.id.mit)
        mit.setOnClickListener {
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

        val github = findViewById<LinearLayout>(R.id.github)
        github.setOnClickListener {
            val link = "https://github.com/gpillusion/CheckFirm"
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
}