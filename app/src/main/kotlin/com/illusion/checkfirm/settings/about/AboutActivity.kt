package com.illusion.checkfirm.settings.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.illusion.checkfirm.BuildConfig
import com.illusion.checkfirm.R
import com.illusion.checkfirm.dialogs.LegalDialog
import com.illusion.checkfirm.utils.Tools
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class AboutActivity : AppCompatActivity() {

    private lateinit var latest: MaterialTextView
    private lateinit var update: MaterialButton
    private lateinit var progress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.title = ""
        setSupportActionBar(toolbar)

        val version = findViewById<MaterialTextView>(R.id.version)
        version.text = BuildConfig.VERSION_NAME

        progress = findViewById(R.id.progress)
        latest = findViewById(R.id.latest)
        update = findViewById(R.id.update)
        if (Tools.isOnline(applicationContext)) {
            backgroundTask()
        } else {
            progress.visibility = View.GONE
            latest.text = getString(R.string.check_network)
            latest.visibility = View.VISIBLE
            update.visibility = View.GONE
        }
        update.setOnClickListener {
            val goPlayStore = Intent(Intent.ACTION_VIEW)
            goPlayStore.data = Uri.parse("market://details?id=" + applicationContext.packageName)
            startActivity(goPlayStore)
        }

        val contributor = findViewById<MaterialButton>(R.id.contributor)
        contributor.setOnClickListener {
            val intent = Intent(this, ContributorActivity::class.java)
            startActivity(intent)
        }

        val legal = findViewById<MaterialButton>(R.id.legal)
        legal.setOnClickListener {
            val bottomSheetFragment = LegalDialog()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        val license = findViewById<MaterialButton>(R.id.license)
        license.setOnClickListener {
            val intent = Intent(this, LicenseActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_about, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_information -> {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
                return true
            }
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun backgroundTask() {
        CoroutineScope(Dispatchers.Main).launch {
            var data = 100
            withContext(Dispatchers.IO) {
                val doc = Jsoup.connect("https://github.com/gpillusion/CheckFirm/blob/master/VERSION").get()
                val version = doc.select("#LC4")

                for (el in version) {
                    data = Integer.parseInt(el.text())
                }
            }

            progress.visibility = View.GONE
            if (BuildConfig.VERSION_CODE >= data) {
                latest.visibility = View.VISIBLE
                update.visibility = View.GONE
            } else {
                latest.visibility = View.GONE
                update.visibility = View.VISIBLE
            }
        }
    }
}