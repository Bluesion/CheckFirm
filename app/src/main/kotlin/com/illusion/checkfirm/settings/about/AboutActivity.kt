package com.illusion.checkfirm.settings.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.illusion.checkfirm.BuildConfig
import com.illusion.checkfirm.R
import com.illusion.checkfirm.databinding.ActivityAboutBinding
import com.illusion.checkfirm.dialogs.LegalDialog
import com.illusion.checkfirm.utils.Tools
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = ""
        setSupportActionBar(binding.toolbar)

        binding.version.text = BuildConfig.VERSION_NAME

        val progress = binding.progress
        val latest = binding.latest
        val update = binding.update
        if (Tools.isOnline(applicationContext)) {
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

        binding.contributor.setOnClickListener {
            val intent = Intent(this, ContributorActivity::class.java)
            startActivity(intent)
        }

        binding.legal.setOnClickListener {
            val bottomSheetFragment = LegalDialog()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        binding.license.setOnClickListener {
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
}