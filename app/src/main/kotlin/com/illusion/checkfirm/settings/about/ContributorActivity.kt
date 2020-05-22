package com.illusion.checkfirm.settings.about

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.trusted.TrustedWebActivityIntentBuilder
import com.google.android.material.appbar.MaterialToolbar
import com.google.androidbrowserhelper.trusted.TwaLauncher
import com.illusion.checkfirm.R
import com.illusion.checkfirm.etc.WebViewActivity

class ContributorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contributor)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.title = getString(R.string.contributor)
        setSupportActionBar(toolbar)

        val designer = findViewById<LinearLayout>(R.id.designer)
        designer.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "plain/text"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("work@newfitlab.com"))
            startActivity(intent)
        }

        val developer = findViewById<LinearLayout>(R.id.developer)
        developer.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "plain/text"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("dnjscjf098@gmail.com"))
            startActivity(intent)
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